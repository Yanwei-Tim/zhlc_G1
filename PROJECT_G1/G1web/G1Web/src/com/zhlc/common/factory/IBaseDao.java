package com.zhlc.common.factory;


import java.util.List;
import java.util.Map;

import com.zhlc.common.codec.Page;
import com.zhlc.common.codec.SearchDTO;
/**
 * @author anquan
 * @Desc dao的公用接口
 * @date 2014-9-12 下午03:34:06
 */
public interface IBaseDao<T> {
	
	/**
	 * 分页查询
	 * @param searchSql 查询语句路径
	 * @param totalCountSql 求总数语句路径
	 * @param searchDTO 查询条件 
	 * @return Page
	 */
	public Page queryPage(String NAMESPACE, String searchSql, String totalCountSql, SearchDTO searchDTO);
	
	/**
	 * 已知总记录数的分页查询
	 * @param searchSql 查询语句路径
	 * @param totalCount 已知总数
	 * @param searchDTO	查询条件 
	 * @return Page
	 */
	public Page queryPageKnowCounts(String NAMESPACE, String searchSql, int totalCount, SearchDTO searchDTO);
	
	/**
	 * 保存实体
	 * @param dto
	 */
	public boolean insert(String NAMESPACE, String sqlName, T dto);
    
    /**
     * 批量保存实体
     * @param List<T>
     */
    public void insertAll(String NAMESPACE, String sqlName, List<T> dtoList);
    
	/**
	 * 根据条件删除
	 * @param dto
	 */
	public void delete(String NAMESPACE, String sqlName, T dto);
	
	/**
	 * 根据id删除
	 * @param id
	 */
	public void deleteById(String NAMESPACE, String sqlName, String id);
    
	/**
	 * 更新
	 * @param dto
	 */
	public void update(String NAMESPACE, String sqlName, T dto);
	
	/**
	 * 有条件查询
	 * @param searchSql
	 * @param id
	 * @return
	 */
	public T queryById(String NAMESPACE, String searchSql, String id);
	
	/**
	 * 查询所有
	 * @param searchSql
	 * @return
	 */
	public List<T> queryAll(String NAMESPACE, String searchSql);
	
	/**
	 * 有条件查询
	 * @param str
	 * @return
	 */
	public List<T> queryAllByStr(String NAMESPACE, String searchSql, String str);
	
	/**
	 * 有条件查询
	 * @param searchSql
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<T> queryAllByMap(String NAMESPACE, String searchSql, Map map);
	
	/**
	 * 有条件查询
	 * @param searchSql
	 * @param dto
	 * @return
	 */
	public List<T> queryAllByParam(String NAMESPACE, String searchSql, T dto);
	
	/**
	 * 返回单个字符串
	 * @param searchSql
	 * @param String
	 * @return
	 */
	public String queryByParam(String NAMESPACE, String searchSql, String param);
	
	/**
	 * 返回单个字符串
	 * @param searchSql
	 * @param Map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String queryByMap(String NAMESPACE, String searchSql, Map map);
	
}
