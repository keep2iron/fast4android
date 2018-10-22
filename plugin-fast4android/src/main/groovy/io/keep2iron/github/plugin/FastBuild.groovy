package io.keep2iron.github.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.FeatureExtension
import com.android.build.gradle.FeaturePlugin
import com.android.build.gradle.LibraryExtension
import groovy.json.StringEscapeUtils
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.api.BaseVariant
import org.gradle.api.artifacts.dsl.DependencyHandler

import java.util.concurrent.atomic.AtomicBoolean
import com.android.build.gradle.api.BaseVariantOutput
import org.gradle.api.DomainObjectSet
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

class FastBuild implements Plugin<Project> {
    public final static String PROPERTY_MAIN_MODULE_NAME = "MAIN_MODULE_NAME"
    public final static String PROPERTY_IS_RUN_ALONE = "IS_RUN_ALONE"
    public final static String PROPERTY_COMPONENT_NAMES = "COMPONENT_NAMES"

    public final static String DEFAULT_MAIN_MODULE_NAME = "module-main"

    //主模块
    String compileModuleName = "module-main"

    String currentModuleName            //当前编译的模块
    boolean isRunAlone                  //是否是独立运行

    @Override
    void apply(Project project) {
        project.task('buildCompDir', type: FastTask)
        //为依赖插件的project进行扩展相关的属性,属性的分配根据FastExtensions这个类进行配置
//        def fastBuildParams = project.extensions.create("componentNames", FastExtensions)
        //拿到相关的任务集合
        String taskNames = project.gradle.startParameter.taskNames.toString()
        System.out.println(taskNames)
        currentModuleName = project.path.replace(":", "")
        System.out.println("current module name : " + currentModuleName)

        AssembleTask task = buildAssembleTask(project.gradle.startParameter.taskNames)
        adjustProperty(project, task)
        buildWithProperty(project, task)

//        def outThis = this
//        project.plugins.all {
//            if(it instanceof FeaturePlugin){
//                FeatureExtension fe = project.extensions.getByType(FeatureExtension.class)
//                outThis.configureR2Generation(project, fe.getFeatureVariants())
//                outThis.configureR2Generation(project, fe.getFeatureVariants())
//            }else if(it instanceof LibraryPlugin){
//                LibraryExtension le = project.extensions.getByType(LibraryExtension.class)
//                outThis.configureR2Generation(project, le.getLibraryVariants())
//            }else if(it instanceof AppPlugin){
//                AppExtension ae = project.extensions.getByType(AppExtension.class)
//                outThis.configureR2Generation(project, ae.getApplicationVariants())
//            }
//        }
    }

    private AssembleTask buildAssembleTask(List<String> taskNames) {
        AssembleTask assembleTask = new AssembleTask()
        for (String task : taskNames) {
            if (task.toUpperCase().contains("ASSEMBLE") ||      //是否是打包命令
                    task.contains("aR") ||                          //同样是否是打包命令
                    task.toUpperCase().contains("RESGUARD")) {       //是否是多渠道生成命令
                assembleTask.isAssemble = true

                //因为打包的命令格式是    gradle module-main:assembleRelease = >[:module-share:assembleDebug]
                //倒数第二个是模块的项目名称
                String[] args = task.split(":")
                assembleTask.modules.add(args.length > 1 ? args[args.length - 2] : "all")
                break
            }
        }

        return assembleTask
    }

    /**
     * 构建主工程目录,已经验证相关gradle属性设置
     *
     * assembleRelease ---module-main
     * app:assembleRelease :module-main:assembleRelease ---app
     * sharecomponent:assembleRelease :sharecomponent:assembleRelease ---sharecomponent
     */
    private void adjustProperty(Project project, AssembleTask assembleTask) {

        if (!project.rootProject.hasProperty(PROPERTY_MAIN_MODULE_NAME)) {
            project.setProperty(PROPERTY_MAIN_MODULE_NAME, DEFAULT_MAIN_MODULE_NAME)
            System.out.println("you should set " + PROPERTY_MAIN_MODULE_NAME + "in root project's gradle.properties")
        }

        if (assembleTask.modules.size() > 0 && assembleTask.modules.get(0) != null
                && assembleTask.modules.get(0).trim().length() > 0
                && assembleTask.modules.get(0) != "all") {
            compileModuleName = assembleTask.modules.get(0)
        } else {
            compileModuleName = project.rootProject.property(PROPERTY_MAIN_MODULE_NAME)
        }

        if (compileModuleName == null || compileModuleName.trim().length() <= 0) {
            compileModuleName = DEFAULT_MAIN_MODULE_NAME
        }

        //如果是主工程可以不用写isRunAlone
        if (!project.hasProperty(PROPERTY_IS_RUN_ALONE) && currentModuleName != DEFAULT_MAIN_MODULE_NAME) {
            project.setProperty(PROPERTY_IS_RUN_ALONE, false)
            System.out.println(currentModuleName + "'s gradle.properties must have " + PROPERTY_IS_RUN_ALONE + "property")
        }

        if (currentModuleName != DEFAULT_MAIN_MODULE_NAME) {
            isRunAlone = Boolean.parseBoolean((project.properties.get(PROPERTY_IS_RUN_ALONE)))
        } else {
            isRunAlone = true
        }

        System.out.println("compile module is : " + compileModuleName)
    }

    /**
     * 通过之前的属性进行项目的构建,编译library生成aar。
     */
    private void buildWithProperty(Project project, AssembleTask assembleTask) {
        //如果运行的是打包任务,只有是主模块和编译模块，isRunAlone修改为true，其他组件都强制修改为false
        if (isRunAlone && assembleTask.isAssemble) {
            if (currentModuleName == compileModuleName || currentModuleName || DEFAULT_MAIN_MODULE_NAME)
                isRunAlone = true
            else
                isRunAlone = false
        }

        if (isRunAlone) {
            project.apply plugin: "com.android.application"
            System.out.println("apply plugin is " + 'com.android.application')

            //如果是组件的项目进行独立运行，如果是主工程则进行将生成的aar包，添加依赖
            if (currentModuleName != DEFAULT_MAIN_MODULE_NAME) {
                project.android.sourceSets {
                    main {
                        manifest.srcFile 'src/comp/AndroidManifest.xml'
                        java.srcDirs = ['src/main/java', 'src/comp/java']
                        res.srcDirs = ['src/main/res', 'src/comp/res']
                    }
                }
            } else if (currentModuleName == DEFAULT_MAIN_MODULE_NAME &&
                    assembleTask.isAssemble) {
                compileComponents(assembleTask, project)
            }
        } else {
            project.apply plugin: "com.android.library"
            System.out.println("apply plugin is " + 'com.android.library')
            //afterEvaluate回调将会执行在当前build.gradle脚本执行完成之后，进行回调，这里相当于是，
            //当library进行编译构建完成之后,将打包好的aar文件进行复制到外部的一个工程目录下，以便外部工程进行调用
            project.afterEvaluate {
                //一个Task都是由多个Action组成的
                Task assembleReleaseTask = project.tasks.findByPath("assembleRelease")
                if (assembleReleaseTask != null) {
                    //找到编译打包任务，添加Action，当编译任务完成后，将生成的aar文件复制出来.
                    assembleReleaseTask.doLast {
                        File infile = new File("build/outputs/aar/$currentModuleName-release.aar")
                        File outfile = new File("../components")
                        project.copy {
                            from infile
                            into outfile
                            rename {
                                String filename -> "$currentModuleName-release.aar"
                            }
                        }
                        System.out.println("$currentModuleName-release.aar copy success ")
                    }
                }
            }
        }
    }

    private void compileComponents(AssembleTask assembleTask, Project project) {
        String components = project.property(PROPERTY_COMPONENT_NAMES)
        if (components != null) {
            for (String component : components.split(",")) {
                if (component == project.property(PROPERTY_MAIN_MODULE_NAME)) continue

                File aar = new File(project.getRootDir(), "components/" + component + "-release.aar")
                if (aar.exists()) {
                    System.out.println(" =================================================================")
                    System.out.println(" =================================================================")
                    System.out.println(currentModuleName + " add aar dependency " + component + "-release.aar")
                    System.out.println(" =================================================================")
                    System.out.println(" =================================================================")

//                    def dependencies = project.dependencies;
                    project.dependencies.add("api", [
                            name: "$component-release",
                            ext : 'aar']){
                        transitive=true
                    }
                } else {
                    throw new IllegalArgumentException("$component-release.aar not found")
                }
            }
        }
    }

    void configureR2Generation(Project project, DomainObjectSet<? extends BaseVariant> variants) {
        variants.all { variant ->
            def outputDir = new File(project.buildDir.getAbsolutePath() + File.separatorChar + "generated/source/r2/${variant.dirName}")
            def task = project.tasks.create("generate${variant.name.capitalize()}R2")
            task.outputs.dir(outputDir)
            variant.registerJavaGeneratingTask(task, outputDir)
            AtomicBoolean once = new AtomicBoolean()
            variant.outputs.all { output ->
                def processResources = output.processResources
                task.dependsOn(processResources)

                // Though there might be multiple outputs, their R files are all the same. Thus, we only
                // need to configure the task once with the R.java input and action.
                if (once.compareAndSet(false, true)) {
                    def rPackage = processResources.originalApplicationId
                    def pathToR = rPackage.replace(".", "" + File.separatorChar)
                    def rFile = new File(processResources.sourceOutputDir.getAbsolutePath() + File.separatorChar + pathToR, "R.java")

                    task.inputs.file(rFile)
                    task.doLast {
                        FinalRClassBuilder.brewJava(rFile, outputDir, rPackage, "R2")
                    }
                }
            }
        }
    }

    class AssembleTask {

        //是否是打包任务
        boolean isAssemble = false

        List<String> modules = new ArrayList<>()
    }
}