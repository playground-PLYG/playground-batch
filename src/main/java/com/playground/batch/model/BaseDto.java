package com.playground.batch.model;

import java.io.Serial;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.util.ReflectionUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.playground.batch.annotation.Secret;
import com.playground.batch.constants.PlaygroundBatchConstants;
import com.playground.batch.utils.MaskingUtil;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class BaseDto implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  @Override
  public String toString() { // NOSONAR
    Map<String, Object> map = new HashMap<>();
    Field[] fields = FieldUtils.getAllFields(getClass());
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    if (ObjectUtils.isNotEmpty(fields)) {
      for (Field field : fields) {
        String fieldName = field.getName();
        String fieldValue = "";

        Annotation annotation = field.getAnnotation(Secret.class);

        try {
          ReflectionUtils.makeAccessible(field);
          fieldValue = StringUtils.defaultString(Objects.toString(field.get(this)));
        } catch (IllegalAccessException e) {
          // Do nothing
        }

        if (annotation != null && StringUtils.isNotBlank(fieldValue)) {
          if (fieldValue.matches(PlaygroundBatchConstants.RegexPattern.RESIDENT_FOREIGNER_REGISTRATION_NUMBER)) {
            fieldValue = MaskingUtil.residentForeignerRegistrationNumber(fieldValue);
            // TODO 전화, 카드, 계좌 등등 마스킹 처리 //NOSONAR
          } else if (!StringUtils.isBlank(fieldValue)) {
            fieldValue = MaskingUtil.withoutFirstAndLast(fieldValue);
          }
        }

        map.put(fieldName, fieldValue);
      }
    }

    try {
      return objectMapper.writeValueAsString(map);
    } catch (JsonProcessingException e) {
      return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }
  }
}
