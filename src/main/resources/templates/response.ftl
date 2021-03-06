package ${packageName};

import org.springframework.hateoas.server.core.Relation;

import com.github.barry.akali.base.dto.BaseResponseDto;
<#if hasDateParam>
import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.barry.akali.base.utils.IConstants;
</#if>
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
<#list imports as import>
import ${import};
</#list>

/**
 *  ${entity.className} generated by auto
 * ${entity.comment}
 *
 * @author ${author}
 * @since ${date}.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "resources")
public class ${className} extends BaseResponseDto {

    /**
     * 
     */
    private static final long serialVersionUID = -1L;

    <#if entity.fields?? && (entity.fields?size > 0)>
    <#list entity.fields as f>
    /**
     * ${f.comment}
     */
    <#if f.className == 'LocalDateTime'>
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = IConstants.DATE_TIME_MS_FORMAT)
    </#if>
    private ${f.className} ${f.name};

    </#list>
    </#if>
}