package cn.yiidii.base.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.yiidii.base.domain.dto.ProcessResultDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Process执行脚本工具类
 *
 * @author ed w
 */
@SuppressWarnings("unused")
public class ProcessUtil {

    /**
     * 执行脚本，结果输出为String
     *
     * @param cmd 命令
     * @return {@link ProcessResultDTO}
     */
    public static ProcessResultDTO<String> execForStr(String cmd) {
        Process process = null;
        try {
            process = RuntimeUtil.exec(cmd);
            int exitCode = process.waitFor();
            String result = RuntimeUtil.getResult(process);
            return new ProcessResultDTO<>(exitCode, StrUtil.trim(result), StrUtil.trim(result), "");
        } catch (Throwable e) {
            return ProcessResultDTO.error(-1, null, ExceptionUtil.getSimpleMessage(e));
        } finally {
            RuntimeUtil.destroy(process);
        }
    }


    /**
     * 执行脚本，结果输出为list
     *
     * @param cmd 命令
     * @return {@link ProcessResultDTO}
     */
    public static ProcessResultDTO<List<String>> execForLines(String cmd) {
        Process process = null;
        try {
            process = RuntimeUtil.exec(cmd);
            int exitCode = process.waitFor();
            List<String> result = RuntimeUtil.getResultLines(process);
            return new ProcessResultDTO<>(exitCode, result, CollUtil.join(result, StrPool.CRLF), "");
        } catch (Throwable e) {
            return ProcessResultDTO.error(-1, new ArrayList<>(), ExceptionUtil.getSimpleMessage(e));
        } finally {
            RuntimeUtil.destroy(process);
        }
    }


    /**
     * 执行脚本，结果转换成对应Class
     *
     * @param cmd 命令
     * @return {@link ProcessResultDTO}
     */
    public static <T> ProcessResultDTO<T> exec(String cmd, Class<T> clazz) {
        Process process = null;
        try {
            process = RuntimeUtil.exec(cmd);
            int exitCode = process.waitFor();
            String result = RuntimeUtil.getResult(process);
            return new ProcessResultDTO<>(exitCode, JsonUtils.parseObject(result, clazz), result, "");
        } catch (Throwable e) {
            return ProcessResultDTO.error(-1, null, ExceptionUtil.getSimpleMessage(e));
        } finally {
            RuntimeUtil.destroy(process);
        }
    }

    /**
     * 执行脚本，结果转换成对应List<Class>
     *
     * @param cmd 命令
     * @return {@link ProcessResultDTO}
     */
    public static <T> ProcessResultDTO<List<T>> execForList(String cmd, Class<T> clazz) {
        Process process = null;
        try {
            process = RuntimeUtil.exec(cmd);
            int exitCode = process.waitFor();
            String result = RuntimeUtil.getResult(process);
            return new ProcessResultDTO<>(exitCode, JsonUtils.parseArray(result, clazz), result, "");
        } catch (Throwable e) {
            return ProcessResultDTO.error(-1, new ArrayList<>(), ExceptionUtil.getSimpleMessage(e));
        } finally {
            RuntimeUtil.destroy(process);
        }
    }

}
