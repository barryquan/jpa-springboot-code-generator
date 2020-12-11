package ${packageName};

import javax.persistence.Column;
<#if entity.hasPk>
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
<#else>
import com.github.barry.akali.base.BaseEntity;
</#if>
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
<#list imports as import>
import ${import};
</#list>

/**
 *  
 * ${entity.comment}
 *
 * @author ${author}
 * @since ${date}.
 */
@Data
@Entity
@Table(name = "${entity.tableName}")
@EqualsAndHashCode(callSuper = false)
public class ${className} <#if entity.hasPk>implements Serializable<#else> extends BaseEntity </#if> {

    /**
     * 
     */
    private static final long serialVersionUID = -1L;

    <#if entity.fields?? && (entity.fields?size > 0)>
    <#list entity.fields as f>
    /**
     * ${f.comment}
     */
     <#if f.isPk>
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     </#if>
    @Column(name = "${f.columnName}")
    private ${f.className} ${f.name};

    </#list>
    </#if>
}