package ${packageName};

import com.rong.barry.base.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
<#list imports as import>
import ${import};
</#list>

/**
 *  
 * ${comments}
 *
 * @author ${author}
 * Created On ${date}.
 */
@Data
@Entity
@Table(name = "${entity.tableName}")
@EqualsAndHashCode(callSuper = false)
public class ${className} extends BaseEntity{

    /**
     * 
     */
    private static final long serialVersionUID = -1L;

    <#if entity.fields?? && (entity.fields?size > 0)>
    <#list entity.fields as f>
    /**
     * TODO ${f.comment}
     */
    private ${f.className} ${f.name};

    </#list>
    </#if>
}