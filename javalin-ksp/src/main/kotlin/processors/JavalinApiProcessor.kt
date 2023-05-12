package processors

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo

class JavalinApiProcessor(private val logger: KSPLogger,
                          private val codeGenerator: CodeGenerator,
                          options: Map<String, String>) : SymbolProcessor {
    internal val mapOfClasses = mutableMapOf<String, ClassName>()
    private val fileSpec: FileSpec.Builder =
        FileSpec.builder(options[OPTION_GENERATED_PACKAGE] ?: "", options[OPTION_GENERATED_CLASS] ?: DEFAULT_CLASS_NAME)
    private val injectAnnotation = AnnotationSpec.builder(ClassName(INJECT_PACKAGE, INJECT_ANNOTATION)).build()
    internal val constructorSpec =
        FunSpec.constructorBuilder().addAnnotation(injectAnnotation).addParameter(JAVALIN_PARAM, ClassName(JAVALIN_PACKAGE, JAVALIN_NAME))
    private val classSpec = TypeSpec.classBuilder(options[OPTION_GENERATED_CLASS] ?: DEFAULT_CLASS_NAME).addProperty(
        PropertySpec.builder(JAVALIN_PARAM, ClassName(JAVALIN_PACKAGE, JAVALIN_NAME), KModifier.PRIVATE).initializer(JAVALIN_PARAM).build()
    )

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val annotationName = "$OPEN_API_PACKAGE.$OPEN_API"
        val symbols = resolver.getSymbolsWithAnnotation(annotationName)
        val dependencies = Dependencies(true, *resolver.getAllFiles().toList().toTypedArray())
        symbols.filter { it is KSFunctionDeclaration && it.validate() }.forEach { it.accept(FunctionVisitor(dependencies), Unit) }
        return symbols.filterNot { it.validate() }.toList()
    }

    private inner class FunctionVisitor(val dependencies: Dependencies) : KSVisitorVoid() {

        @Suppress("UNCHECKED_CAST")
        override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit) {
            val annotation = function.annotations.first { it.shortName.asString() == OPEN_API }
            val classParam = function.parentDeclaration?.simpleName?.asString()?.let {
                val result = "${it.substring(0, 1).lowercase()}${it.substring(1)}"
                val className = ClassName(
                    function.parentDeclaration?.packageName?.asString()!!,
                    function.parentDeclaration?.simpleName?.asString()!!
                )
                mapOfClasses[result] = className
                result
            }

            var path = ""
            var methods = arrayListOf<KSType>()
            annotation.arguments.forEach {
                when (it.name?.getShortName()) {
                    OPEN_API_PARAM_PATH -> path = it.value.toString()
                    OPEN_API_PARAM_METHOD -> {
                        methods = it.value as ArrayList<KSType>
                    }
                }
            }
            methods.forEach {
                constructorSpec.addCode(CodeBlock.builder().addStatement(statement(it, path, classParam, function)).build())
            }
        }

        private fun statement(it: KSType, path: String, classParam: String?, function: KSFunctionDeclaration) =
            "$JAVALIN_PARAM.${it.toClassName().simpleName.lowercase()}(\"${path}\", ${classParam}.${function.simpleName.asString()}())"
    }

    override fun finish() {
        mapOfClasses.entries.forEach {
            constructorSpec.addParameter(it.key, it.value)
            classSpec.addProperty(PropertySpec.builder(it.key, it.value, KModifier.PRIVATE).initializer(it.key).build())
        }
        classSpec.primaryConstructor(constructorSpec.build())
        fileSpec.addType(classSpec.build()).build().writeTo(codeGenerator, Dependencies(true))
    }

    companion object {
        const val OPEN_API = "OpenApi"
        const val OPEN_API_PACKAGE = "io.javalin.openapi"
        const val OPTION_GENERATED_PACKAGE = "javalin.processor.generated.package"
        const val OPTION_GENERATED_CLASS = "javalin.processor.generated.class"
        const val DEFAULT_CLASS_NAME = "JavalinApi"
        const val JAVALIN_PACKAGE = "io.javalin"
        const val JAVALIN_NAME = "Javalin"
        const val JAVALIN_PARAM = "javalin"
        const val OPEN_API_PARAM_PATH = "path"
        const val OPEN_API_PARAM_METHOD = "methods"
        const val INJECT_PACKAGE = "javax.inject"
        const val INJECT_ANNOTATION = "Inject"
    }
}