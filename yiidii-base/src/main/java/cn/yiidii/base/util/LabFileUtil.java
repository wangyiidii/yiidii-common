package cn.yiidii.base.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.io.InputStream;
import java.util.Objects;

/**
 * 文件工具类
 *
 * @author ed w
 * @since 1.0
 */
public class LabFileUtil {

    /**
     * 从classPath下获取文件到user.dir同级目录的var目录
     *
     * @param filePath classPath目录
     * @param override 是否覆盖
     * @return File
     */
    public static File getVarFileFromClassPath(String filePath, boolean override) {
        return getFileFromClassPath(filePath, "var", override);
    }

    /**
     * 从classPath下获取文件到user.dir同级目录的module目录
     *
     * @param filePath classPath目录
     * @param module   子目录
     * @param override 是否覆盖
     * @return File
     */
    public static File getFileFromClassPath(String filePath, String module, boolean override) {
        String dir = System.getProperty("user.dir")
                .concat(StrUtil.isNotBlank(module) ? File.separator.concat(module) : "")
                .concat(File.separator).concat(filePath.substring(0, filePath.lastIndexOf(File.separator)));
        String fileName = FileNameUtil.getPrefix(filePath);
        String suffix = FileNameUtil.getSuffix(filePath);

        File ret = FileUtil.file(dir, fileName.concat(StrPool.DOT).concat(suffix));
        try {
            FileUtil.mkdir(dir);
            if (ret.exists()) {
                if (override) {
                    FileUtil.del(ret);
                    ret.createNewFile();
                } else {
                    return ret;
                }
            } else {
                ret.createNewFile();
            }
        } catch (Exception e) {
            return null;
        }

        InputStream in = LabFileUtil.class.getResourceAsStream(filePath);
        if (Objects.isNull(in)) {
            return null;
        }

        FileUtil.writeFromStream(in, ret);
        return ret;
    }
}
