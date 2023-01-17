package cn.sunyc.ddnsgeneral.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 主动调用的校验工具
 */
@Slf4j
public class ValidateUtil {


    @SuppressWarnings("all")
    private static final SpringValidatorAdapter VALIDATOR = new SpringValidatorAdapter(Validation.buildDefaultValidatorFactory().getValidator());

    /**
     * Spring校验器，校验失败直接抛异常
     *
     * @param entry 参数
     */
    public static void validate(Object entry) {
        final Errors errors = new BeanPropertyBindingResult(entry, entry.getClass().getName());
        VALIDATOR.validate(entry, errors);
        if (!errors.getAllErrors().isEmpty()) {
            log.error(errors.toString());
            throw new IllegalArgumentException(errors.getAllErrors().toString());
        }
    }

    /**
     * JSR校验器，校验失败直接抛异常
     *
     * @param t   参数
     * @param <T> 参数类型
     */
    public static <T> void valid(T t) throws IllegalArgumentException {
        final List<String> invalidList = validDesc(t);
        if (!invalidList.isEmpty()) {
            throw new IllegalArgumentException(invalidList.get(0));
        }
    }

    /**
     * JSR校验器
     *
     * @param t   参数
     * @param <T> 参数类型
     * @return 校验失败的结果列表
     */
    public static <T> List<String> validDesc(T t) {
        try (final ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            final Validator validator = factory.getValidator();
            final Set<ConstraintViolation<T>> errors = validator.validate(t);
            return errors.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }


    private ValidateUtil() {
    }
}
