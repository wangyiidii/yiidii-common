package cn.yiidii.web.config;

import cn.hutool.core.util.StrUtil;
import cn.yiidii.base.R;
import cn.yiidii.base.exception.BaseUncheckedException;
import cn.yiidii.base.exception.BizException;
import cn.yiidii.base.exception.RateLimitException;
import cn.yiidii.base.exception.code.ExceptionCode;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 全局异常统一处理
 *
 * @author zuihou
 */
@Slf4j
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@RestControllerAdvice
@RequiredArgsConstructor
public abstract class GlobalExceptionHandler {

    public final HttpServletRequest request;

    @PostConstruct
    private void init() {
        log.info("Condition type is WebApplication, init GlobalExceptionHandler");
    }

    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.OK)
    public R<?> bizException(BizException ex) {
        log.error("请求地址: {}, 业务异常: {}", request.getRequestURI(), ex.getMessage());
        log.debug("请求地址: {}, 业务异常: {}", request.getRequestURI(), ex);
        return R.failed(ex.getCode(), ex.getMessage(), ex.getData());
    }

    @ExceptionHandler(RateLimitException.class)
    @ResponseStatus(HttpStatus.OK)
    public R<?> rateLimitException(RateLimitException ex) {
        log.error("请求地址: {}, 限流异常: {}", request.getRequestURI(), ex.getMessage());
        log.debug("请求地址: {}, 限流异常: {}", request.getRequestURI(), ex);
        return R.failed(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(BaseUncheckedException.class)
    @ResponseStatus(HttpStatus.OK)
    public R<?> baseUncheckedException(BaseUncheckedException ex) {
        log.error("请求地址: {}, baseUncheckedException: {}", request.getRequestURI(), ex.getMessage());
        log.debug("请求地址: {}, baseUncheckedException: {}", request.getRequestURI(), ex);
        return R.failed(ex.getCode());
    }

    // ############################## 4xx start ##############################

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<?> httpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("请求地址: {}, 参数异常: {}", request.getRequestURI(), ex.getMessage());
        log.debug("请求地址: {}, 参数异常: {}", request.getRequestURI(), ex);

        String message;
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException) {
            InvalidFormatException exception = ((InvalidFormatException) cause);
            Object value = exception.getValue();
            Class<?> targetType = exception.getTargetType();

            message = StrUtil.format("参数:[{}]的传入值:[{}]与预期的字段类型[{}]不匹配.", exception.getPath().get(0).getFieldName(), value, targetType.getSimpleName());
            return R.failed(ExceptionCode.PARAM_EX.getCode(), message);
        } else {
            message = ex.getMessage();
            if (StrUtil.containsAny(message, "Could not read document:")) {
                String msg = StrUtil.format("无法正确的解析json类型的参数: {}", StrUtil.subBetween(message, "Could not read document:", " at "));
                return R.failed(ExceptionCode.PARAM_EX.getCode(), msg);
            }
            return R.failed(ExceptionCode.PARAM_EX.getCode(), ExceptionCode.PARAM_EX.getMsg());
        }


    }


    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<?> bindException(BindException ex) {
        log.error("请求地址: {}, 参数校验异常: {}", request.getRequestURI(), ex.getMessage());
        log.debug("请求地址: {}, 参数校验异常: {}", request.getRequestURI(), ex);
        try {
            String msg = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
            if (StrUtil.isNotEmpty(msg)) {
                return R.failed(ExceptionCode.PARAM_EX.getCode(), msg);
            }
        } catch (Exception ee) {
            log.debug("获取异常描述失败", ee);
        }
        StringBuilder msg = new StringBuilder();
        List<FieldError> fieldErrors = ex.getFieldErrors();
        fieldErrors.forEach((oe) ->
                msg.append("参数:[").append(oe.getObjectName())
                        .append(".").append(oe.getField())
                        .append("]的传入值:[").append(oe.getRejectedValue()).append("]与预期的字段类型不匹配.")
        );
        return R.failed(ExceptionCode.PARAM_EX.getCode(), msg.toString());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<?> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.error("请求地址: {}, 参数类型校验异常: {}", request.getRequestURI(), ex.getMessage());
        log.debug("请求地址: {}, 参数类型校验异常: {}", request.getRequestURI(), ex);
        String msg = "参数：[" + ex.getName() + "]的传入值：[" + ex.getValue() +
                "]与预期的字段类型：[" + Objects.requireNonNull(ex.getRequiredType()).getName() + "]不匹配";
        return R.failed(ExceptionCode.PARAM_EX.getCode(), msg);
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<?> illegalStateException(IllegalStateException ex) {
        log.error("请求地址: {}, 不合法状态: {}", request.getRequestURI(), ex.getMessage());
        log.debug("请求地址: {}, 不合法状态: {}", request.getRequestURI(), ex);
        return R.failed(ExceptionCode.PARAM_EX.getCode(), ExceptionCode.PARAM_EX.getMsg());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<?> missingServletRequestParameterException(MissingServletRequestParameterException ex) {
        log.error("请求地址: {}, 缺少参数异常: {}", request.getRequestURI(), ex.getMessage());
        log.debug("请求地址: {}, 缺少参数异常: {}", request.getRequestURI(), ex);
        return R.failed(ExceptionCode.PARAM_EX.getCode(), "缺少必须的[" + ex.getParameterType() + "]类型的参数[" + ex.getParameterName() + "]");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<?> illegalArgumentException(IllegalArgumentException ex) {
        log.error("请求地址: {}, 无效参数异常: {}", request.getRequestURI(), ex.getMessage());
        log.debug("请求地址: {}, 无效参数异常: {}", request.getRequestURI(), ex);
        return R.failed(ExceptionCode.PARAM_EX.getCode(), ex.getMessage());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<?> httpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        log.error("请求地址: {}, 无效的Content-Type类型: {}", request.getRequestURI(), ex.getMessage());
        log.debug("请求地址: {}, 无效的Content-Type类型: {}", request.getRequestURI(), ex);
        MediaType contentType = ex.getContentType();
        if (contentType != null) {
            return R.failed(ExceptionCode.PARAM_EX.getCode(), "请求类型(Content-Type)[" + contentType + "] 与实际接口的请求类型不匹配");
        }
        return R.failed(ExceptionCode.PARAM_EX.getCode(), "无效的Content-Type类型");
    }

    @ExceptionHandler({MissingServletRequestPartException.class, MultipartException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<?> missingServletRequestPartException(MissingServletRequestPartException ex) {
        log.error("请求地址: {}, 请求中必须至少包含一个有效文件: {}", request.getRequestURI(), ex.getMessage());
        log.debug("请求地址: {}, 请求中必须至少包含一个有效文件: {}", request.getRequestURI(), ex);
        return R.failed(ExceptionCode.REQUIRED_FILE_PARAM_EX.getCode(), ExceptionCode.REQUIRED_FILE_PARAM_EX.getMsg());
    }

    @ExceptionHandler(ServletException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<?> servletException(ServletException ex) {
        log.error("请求地址: {}, ServletException: {}", request.getRequestURI(), ex.getMessage());
        log.debug("请求地址: {}, ServletException: {}", request.getRequestURI(), ex);
        String msg = "UT010016: Not a multi part request";
        if (msg.equalsIgnoreCase(ex.getMessage())) {
            return R.failed(ExceptionCode.REQUIRED_FILE_PARAM_EX.getCode(), ExceptionCode.REQUIRED_FILE_PARAM_EX.getMsg());
        }
        return R.failed(ExceptionCode.PARAM_EX.getCode(), ExceptionCode.PARAM_EX.getMsg());
    }

    /**
     * jsr 规范中的验证异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<?> constraintViolationException(ConstraintViolationException ex) {
        log.error("请求地址: {}, constraintViolationException: {}", request.getRequestURI(), ex.getMessage());
        log.debug("请求地址: {}, constraintViolationException: {}", request.getRequestURI(), ex);
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        String message = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(";"));

        return R.failed(ExceptionCode.PARAM_EX.getCode(), message);
    }

    /**
     * spring 封装的参数验证异常， 在controller中没有写result参数时，会进入
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<?> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("请求地址: {}, methodArgumentNotValidException: {}", request.getRequestURI(), ex.getMessage());
        log.debug("请求地址: {}, methodArgumentNotValidException: {}", request.getRequestURI(), ex);
        return R.failed(ExceptionCode.PARAM_EX.getCode(), Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage());
    }

    //404

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public R<?> noHandlerFoundException(NoHandlerFoundException ex) {
        log.error("请求地址: {}, 无处理器异常: {}", request.getRequestURI(), ex.getMessage());
        log.debug("请求地址: {}, 无处理器异常: {}", request.getRequestURI(), ex);
        return R.failed(ExceptionCode.NOT_FOUND.getCode(), ExceptionCode.NOT_FOUND.getMsg());
    }

    // 405

    /**
     * 返回状态码:405
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public R<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.error("请求地址: {}, 不支持当前请求类型: {}", request.getRequestURI(), ex.getMessage());
        log.debug("请求地址: {}, 不支持当前请求类型: {}", request.getRequestURI(), ex);
        return R.failed(ExceptionCode.METHOD_NOT_ALLOWED.getCode(), ExceptionCode.METHOD_NOT_ALLOWED.getMsg());
    }

    // ############################## 4xx end ##############################


    // ############################## 5xx start ##############################

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<?> nullPointerException(NullPointerException ex) {
        log.error("请求地址: {}, 空指针异常: {}", request.getRequestURI(), ex.getMessage());
        log.debug("请求地址: {}, 空指针异常: {}", request.getRequestURI(), ex);
        return R.failed(ExceptionCode.INTERNAL_SERVER_ERROR.getCode(), ExceptionCode.INTERNAL_SERVER_ERROR.getMsg());
    }

    /**
     * 其他异常
     *
     * @param ex 其他异常
     * @return {@link R}
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<?> otherExceptionHandler(Throwable ex) {
        log.error("请求地址: {}, other Throwable: {}", request.getRequestURI(), ex.getMessage());
        log.debug("请求地址: {}, other Throwable: {}", request.getRequestURI(), ex);
        if (ex.getCause() instanceof BizException) {
            return this.bizException((BizException) ex.getCause());
        }
        return R.failed(ExceptionCode.SYSTEM_EX.getCode(), ExceptionCode.SYSTEM_EX.getMsg());
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<?> sqlException(SQLException ex) {
        log.error("请求地址: {}, SQL异常: {}", request.getRequestURI(), ex.getMessage());
        log.debug("请求地址: {}, SQL异常: {}", request.getRequestURI(), ex);
        return R.failed(ExceptionCode.INTERNAL_SERVER_ERROR.getCode(), ExceptionCode.INTERNAL_SERVER_ERROR.getMsg());
    }

    // ############################## 5xx end ##############################

}
