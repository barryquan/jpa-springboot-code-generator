package com.github.barry.akali.base;

import java.io.Serializable;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 基础JPA的Repository，继承JpaRepositoryImplementation，<br>
 * JpaRepositoryImplementation本身继承JpaRepository和JpaSpecificationExecutor<br>
 * JpaRepository是Spring jpa的基础，但是搜索功能不是很强大<br>
 * JpaSpecificationExecutor 具备强大的搜索功能<br>
 * 用于构造自身基于jpa的项目的基础仓库<br>
 *
 * @param <T>  泛型，数据库的实体
 * @param <ID> 数据库实体的主键
 * @author barry
 */
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepositoryImplementation<T, ID> {

}
