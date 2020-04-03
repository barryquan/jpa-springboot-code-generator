package ${packageName};

import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.barry.akali.base.BaseEndpoint;
import com.github.barry.akali.base.utils.PageInfo;
import com.github.barry.akali.base.utils.RequestSearchUtils;
import com.github.barry.akali.base.dto.ResponseDto;
import ${lastRenderResponse.dto.packageName}.${lastRenderResponse.dto.className};
import ${lastRenderResponse.response.packageName}.${lastRenderResponse.response.className};
import ${lastRenderResponse.service.packageName}.${lastRenderResponse.service.className};

/**
 * controller for ${entity.className}
 * ${comments}
 *
 * @author ${author}
 * Created On ${date}.
 */
@RestController
@RequestMapping("/${entity.className?uncap_first}")
public class ${className} extends BaseEndpoint {

    @Autowired
    private ${lastRenderResponse.service.className} ${lastRenderResponse.service.className?uncap_first};

    /**
     * 新增
     * 
     * @param ${lastRenderResponse.dto.className?uncap_first}
     * @return
     */
    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody ${lastRenderResponse.dto.className} ${lastRenderResponse.dto.className?uncap_first}) {
        ${lastRenderResponse.response.className} ${entity.className?uncap_first}Resp = ${lastRenderResponse.service.className?uncap_first}.create(${lastRenderResponse.dto.className?uncap_first});
        return ResponseEntity.ok(new EntityModel<${lastRenderResponse.response.className}>(${entity.className?uncap_first}Resp, super.getSelfLink(this.getClass(), ${entity.className?uncap_first}Resp.getId())));
    }

    /**
     * 删除
     * 
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseDto<?> delete(@PathVariable ${entity.id.className} id) {
        ${lastRenderResponse.service.className?uncap_first}.deleteById(id);
        return ResponseDto.success(null);
    }

    /**
     * 更新
     * 
     * @param ${lastRenderResponse.dto.className}
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable ${entity.id.className} id,@RequestBody ${lastRenderResponse.dto.className} ${lastRenderResponse.dto.className?uncap_first}) {
        ${lastRenderResponse.response.className} ${entity.className?uncap_first}Resp = ${lastRenderResponse.service.className?uncap_first}.update(id,${lastRenderResponse.dto.className?uncap_first});
        return ResponseEntity.ok(new EntityModel<${lastRenderResponse.response.className}>(${entity.className?uncap_first}Resp, super.getSelfLink(this.getClass(), ${entity.className?uncap_first}Resp.getId())));
    }

    /**
     * 详情
     * 
     * @param id
     * @return
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<?> details(@PathVariable ${entity.id.className} id) {
        ${lastRenderResponse.response.className} ${entity.className?uncap_first}Resp = ${lastRenderResponse.service.className?uncap_first}.details(id);
        return ResponseEntity.ok(new EntityModel<${lastRenderResponse.response.className}>(${entity.className?uncap_first}Resp, super.getSelfLink(this.getClass(), ${entity.className?uncap_first}Resp.getId())));
    }

    @Override
    @GetMapping("")
    public HttpEntity<PagedModel<EntityModel<?>>> getPageData(
            @RequestParam(value = PAGE_NUM, defaultValue = PAGE_NUM_VAL) int pageNumber,
            @RequestParam(value = PAGE_SIZE, defaultValue = PAGE_SIZE_VAL) int pageSize,
            @RequestParam(value = SORTTYPE, defaultValue = SORT_TYPE_VAL) String sortType, ServletRequest request) {
        // 获取搜索参数
        Map<String, Object> searchParams = RequestSearchUtils.getParamStartWith(request, SEARCH_PREFIX1);
        PageInfo pageInfo = new PageInfo(pageNumber, pageSize, sortType);
        Page<${lastRenderResponse.response.className}> page = ${lastRenderResponse.service.className?uncap_first}.getPageList(searchParams, pageInfo);
        return super.doPage(pageNumber, pageSize, sortType, request, this.getClass(), page);
    }

    /**
     * 条件搜索，返回不分页的列表
     * 
     * @param request
     * @return
     */
    @GetMapping("/find/params")
    public ResponseEntity<?> findByParams(ServletRequest request) {
        // 1.获取搜索参数
        Map<String, Object> searchParams = RequestSearchUtils.getParamStartWith(request, SEARCH_PREFIX1);
        // 2.获取数据
        List<${lastRenderResponse.response.className}> dataList = ${lastRenderResponse.service.className?uncap_first}.findByParams(searchParams);
        return super.doListResources(dataList, this.getClass());
    }
}