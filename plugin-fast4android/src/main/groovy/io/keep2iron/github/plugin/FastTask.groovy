package io.keep2iron.github.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import org.w3c.dom.Document
import org.w3c.dom.Element
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class FastTask extends DefaultTask {
    private String mPackageName

    /**
     * 构建comp目录,因为每一次组件化项目可能需要进行创建文件夹，然后创建一些文件
     */
    @TaskAction
    void buildCompDir() {
        Project project = getProject()
        String currentModuleName = project.path.replace(":", "")
        if (currentModuleName.contains(FastBuild.DEFAULT_MAIN_MODULE_NAME)) return

        File compDir = new File(project.getProjectDir(),
                "src" + File.separator +
                        "comp")
        if (!compDir.exists()) compDir.mkdir()
        File projectDir = project.getProjectDir()

        String packagePath = parseAndroidManifest(
                compDir,
                new File(projectDir, "src" + File.separator + "main" + File.separator + "AndroidManifest.xml")
        )

        rebuildAndroidManifest(
                compDir,
                new File(projectDir, "src" + File.separator +
                        "main" + File.separator +
                        "AndroidManifest.xml"),
                mPackageName,
                currentModuleName
        )

        File javaDir = new File(compDir,"java")
        if(!javaDir.exists()) javaDir.mkdir()
        File resDir = new File(compDir,"res")
        if(!resDir.exists()) resDir.mkdir()

        File packageFile = new File(javaDir,packagePath)
        System.out.println(packageFile.getAbsolutePath())
        if (!packageFile.exists()) {
            System.out.println("create " + packageFile.getAbsolutePath() + packageFile.mkdirs() ? " Successful": " Failed")
        }
    }

    private void rebuildAndroidManifest(File compDir, File originAndroidManifest, String packageStr, String moduleName) {
        moduleName = moduleName.split("-")[1]
        moduleName = moduleName.substring(0, 1).toUpperCase() + moduleName.substring(1)
        File androidManifest = new File(compDir, "AndroidManifest.xml")

        if (!androidManifest.exists()) {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance()
            //DOM parser instance
            DocumentBuilder builder = builderFactory.newDocumentBuilder()
            //parse an XML file into a DOM tree
            Document document = builder.parse(originAndroidManifest)
            document.getDocumentElement().setAttribute("package", packageStr)
            Element application = (Element)document.getElementsByTagName("application").item(0)
            application.setAttribute("android:name","." + moduleName + "Application")

            //保存
            TransformerFactory factory = TransformerFactory.newInstance()
            Transformer former = factory.newTransformer()
            former.transform(new DOMSource(document), new StreamResult(androidManifest))
        }
    }

    /**
     * 返回生成的package路径
     *
     * @param compDir   组件化的目录文件
     * @param androidManifestFile AndroidManifest文件目录
     * @return src文件目录
     */
    private String parseAndroidManifest(File compDir, File androidManifestFile) {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance()
        //DOM parser instance
        DocumentBuilder builder = builderFactory.newDocumentBuilder()
        //parse an XML file into a DOM tree
        Document document = builder.parse(androidManifestFile)
        mPackageName = document.getDocumentElement().getAttribute("package")
        String[] split = mPackageName.split("\\.")
        StringBuilder packagePath = new StringBuilder()
        split.each {
            packagePath.append(it).append(File.separator)
        }
        packagePath.append("comp")
//        if (packagePath.length() != 0) packagePath.deleteCharAt(packagePath.length() - 1)

        return packagePath
    }
}