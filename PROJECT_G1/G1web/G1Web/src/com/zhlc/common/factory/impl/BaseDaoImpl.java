package com.zhlc.common.factory.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.zhlc.common.codec.Page;
import com.zhlc.common.codec.SearchDTO;
import com.zhlc.common.constants.Constants;
import com.zhlc.common.exception.BmException;
import com.zhlc.common.factory.IBaseDao;
import com.zhlc.common.utils.LogUtil;
import com.zhlc.common.utils.MyBatisSql;
import com.zhlc.common.utils.MyBatisSqlUtils;

/**
 * @author anquan
 * @Desc: dao的基类.<br>
 * @date 2014-9-12 下午10:38:05
 */
public class BaseDaoImpl<T> extends SqlSessionDaoSupport implements IBaseDao<T> {
	
	private static final Logger LOGGER = LogUtil.getDefaultInstance();
	
    /**
	 * 根据查询条件获取指定记录数(分页只需要起始索引和页大小即可)
	 * @param searchSql
	 * @param totalCountSql
	 * @param searchDTO
	 * @return
     */
	@Override
	public Page queryPage(String NAMESPACE, String searchSql, String totalCountSql, SearchDTO searchDTO) {
		//获取指定条件的总记录数
		int totalCount = getRowCount(NAMESPACE + totalCountSql, searchDTO);
		
		//初始化索引
		searchDTO.setTotal(totalCount);

		String sqlPath = NAMESPACE + searchSql;
		printSql(sqlPath, searchDTO);
		
		List<?> dataRows = getSqlSession().selectList(NAMESPACE + searchSql, searchDTO);
		
		//初始化分页对象
		Page page = new Page(searchDTO.getCurrentPage(), searchDTO.getPageSize(), totalCount, dataRows);
		
		return page;
	}
	
	/**
	 * 已知总记录数的分页查询
	 * @param searchSql
	 * @param totalCount
	 * @param searchDTO
	 */
	@Override
	public Page queryPageKnowCounts(String NAMESPACE, String searchSql, int totalCount, SearchDTO searchDTO) {
		List<?> dataRows = null;
		if(totalCount == 0){
			dataRows = new ArrayList<Object>();
		}else{
			//初始化索引
			searchDTO.setTotal(totalCount);
			dataRows = getSqlSession().selectList(NAMESPACE + searchSql, searchDTO);
		}
		//初始化分页对象
		Page page = new Page(searchDTO.getCurrentPage(), searchDTO.getPageSize(), totalCount, dataRows);
		
		//printSql(searchSqlPath, searchDTO);
		return page;
	}

	/**
	 * 获取总行数
	 * @return
	 */
	private int getRowCount(String rowCountSqlPath, SearchDTO searchDTO) {
		Integer count = (Integer) getSqlSession().selectOne(rowCountSqlPath, searchDTO);
		//printSql(rowCountSqlPath, searchDTO);
		return count;
	}

	/**
	 * 保存实体
	 * @param dto
	 */
	@Override
	public boolean insert(String NAMESPACE, String sqlName, T dto) {
		String sqlPath = NAMESPACE + sqlName;
		int i = 0;
        try {
            i = getSqlSession().insert(sqlPath, dto);
        } catch (Exception e){
            LOGGER.error("新增" + sqlPath + "出现异常：", e);     
            e.printStackTrace();
            throw new BmException(Constants.common_errors_1003);
        }
        if(1 != i){
            throw new BmException(Constants.common_errors_1003);
        }
        return 1 == i ? true : false;
	}
	
	/**
     * 批量保存实体
     * @param List<T>
	 */
	@Override
	public void insertAll(String NAMESPACE, String sqlName, List<T> dtoList) {
		String sqlPath = NAMESPACE + sqlName;
		//int i = 0;
		try {
			if(dtoList != null && dtoList.size() > 0){
				getSqlSession().insert(sqlPath, dtoList);
			}
		} catch (Exception e){
			LOGGER.error("新增" + sqlPath + "出现异常：", e);      
			e.printStackTrace();
			throw new BmException(Constants.common_errors_1003, e);
		}
		/** 批量插入肯定不止一条数据
		if(1 != i){
			LOGGER.error(Constants.common_errors_1003 + "返回操作数量 = " + i);     
			throw new BmException(Constants.common_errors_1003);
		}*/
	}

	/**
	 * 根据条件删除
	 * @param dto
	 */
	@Override
	public void delete(String NAMESPACE, String sqlName, T dto) {
		String sqlPath = NAMESPACE + sqlName;
		int i = 0;
        try {
            i = getSqlSession().delete(sqlPath, dto);
        } catch (Exception e){
            LOGGER.error("删除" + sqlPath + "出现异常：", e);     
            e.printStackTrace();
            throw new BmException(Constants.common_errors_1004);
        }
        if(0 == i){
            throw new BmException(Constants.common_errors_1004);
        }
	}

	/**
	 * 根据id删除
	 * @param id
	 */
	@Override
	public void deleteById(String NAMESPACE, String sqlName, String id) {
		String sqlPath = NAMESPACE + sqlName;
		//int i = 0;
        try {
         	printSql(sqlPath, id);
            getSqlSession().delete(sqlPath, id);
        } catch (Exception e){
            LOGGER.error("删除" + sqlPath + "出现异常：", e);     
            e.printStackTrace();
            throw new BmException(Constants.common_errors_1004);
        }
        /* 
         * 为空或多个删除时就不适合
        if(1 != i){
            throw new BmException(Constants.common_errors_1004);
        }
        */
	}

	/**
	 * 更新
	 * @param dto
	 */
	@Override
	public void update(String NAMESPACE, String sqlName, T dto) {
		String sqlPath = NAMESPACE + sqlName;
		int i = 0;
        try{
            i = getSqlSession().update(sqlPath, dto);
        } catch (Exception e){
        	LOGGER.error("修改" + sqlPath + "出现异常：", e);       
            e.printStackTrace();
            throw new BmException(Constants.common_errors_1005);
        }
        if(1 != i){
            throw new BmException(Constants.common_errors_1005);
        }
	}

	/**
	 * 有条件查询
	 * @param searchSql
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T queryById(String NAMESPACE, String searchSql, String id) {
		String sqlPath = NAMESPACE + searchSql;
		try {
			return (T)getSqlSession().selectOne(sqlPath, id);
		} catch (Exception e) {
			LOGGER.error("查询" + sqlPath + "出现异常：", e);     
            e.printStackTrace();
            throw new BmException(Constants.common_errors_1006);
		}
	}

	/**
	 * 查询所有
	 * @param searchSql
	 * @return
	 */
	@Override
	public List<T> queryAll(String NAMESPACE, String searchSql) {
		String sqlPath = NAMESPACE + searchSql;
		try {
			return getSqlSession().selectList(sqlPath);
		} catch (Exception e) {
			LOGGER.error("查询" + sqlPath + "出现异常：", e);     
            e.printStackTrace();
            throw new BmException(Constants.common_errors_1006);
		}
	}
	/**
	 * 查询所有
	 * @param str
	 * @return
	 */
	@Override
	public List<T> queryAllByStr(String NAMESPACE, String searchSql, String str) {
		String sqlPath = NAMESPACE + searchSql;
		try {
			return getSqlSession().selectList(sqlPath,str);
		} catch (Exception e) {
			LOGGER.error("查询" + sqlPath + "出现异常：", e);     
			e.printStackTrace();
			throw new BmException(Constants.common_errors_1006);
		}
	}
	/**
	 * 有条件查询
	 * @param searchSql
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<T> queryAllByMap(String NAMESPACE, String searchSql, Map map) {
		String sqlPath = NAMESPACE + searchSql;
		try {
			return getSqlSession().selectList(sqlPath, map);
		} catch (Exception e) {
			LOGGER.error("查询" + sqlPath + "出现异常：", e);     
			e.printStackTrace();
			throw new BmException(Constants.common_errors_1006);
		}
	}

	/**
	 * 有条件查询
	 * @param searchSql
	 * @param dto
	 * @return
	 */
	@Override
	public List<T> queryAllByParam(String NAMESPACE, String searchSql, T dto) {
		String sqlPath = NAMESPACE + searchSql;
		try {
			return getSqlSession().selectList(sqlPath, dto);
		} catch (Exception e) {
			LOGGER.error("查询" + sqlPath + "出现异常：", e);     
            e.printStackTrace();
            throw new BmException(Constants.common_errors_1006);
		}
	}
	
	/**
	 * 返回单个字符串
	 * @param searchSql
	 * @param String
	 * @return
	 */
	@Override
	public String queryByParam(String NAMESPACE, String searchSql, String param) {
		String sqlPath = NAMESPACE + searchSql;
		try {
			return String.valueOf(getSqlSession().selectOne(sqlPath, param));
        } catch (Exception e){
        	LOGGER.error("查询" + sqlPath + "出现异常：", e);    
            e.printStackTrace();
            throw new BmException(Constants.common_errors_1006, e);
        }
	}

	/**
	 * 返回单个字符串
	 * @param searchSql
	 * @param Map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public String queryByMap(String NAMESPACE, String searchSql, Map map) {
		String sqlPath = NAMESPACE + searchSql;
		try {
			return String.valueOf(getSqlSession().selectOne(sqlPath, map));
        } catch (Exception e){
        	LOGGER.error("查询" + sqlPath + "出现异常：", e);    
            e.printStackTrace();
            throw new BmException(Constants.common_errors_1006, e);
        }
	}
	
	/**
	 * 打印mybatis的SQL语句到控制台
	 * @param sqlPath
	 * @param ojb 
	 */
	public void printSql(String sqlPath,Object ojb){
        try{
            MyBatisSql myBatisSql = new MyBatisSqlUtils().getMyBatisSql(sqlPath, ojb, getSqlSession());
            System.out.println("========================= start sql ===================");
            if(myBatisSql!=null){
                System.out.println(myBatisSql.toString().replaceAll("\t", ""));
            }
            System.out.println("========================= end sql ===================");
        }catch (Exception e){
            // TODO: handle exception
            e.printStackTrace();
        }
    }
	/**
	 * 获取mybatis的SQL语句
	 * @param str  需要添加的前缀如：新增***：
	 * @param sqlPath  SQL的路径
	 * @param ojb  传入SQL参数
	 * @return 
	 */
	public String getSql(String str,String sqlPath,Object ojb){
	    StringBuffer sb = new StringBuffer();
	    try{
	        MyBatisSql myBatisSql = new MyBatisSqlUtils().getMyBatisSql(sqlPath, ojb, getSqlSession());
	        if(myBatisSql!=null){
	            sb.append(str);
	            sb.append("=========================start sql===================");
	            sb.append(myBatisSql.toString());
	            sb.append("=========================end sql===================");
	        }else{
	            sb.append("没有获取到SQL语句!");
	        }
	    }catch (Exception e){
	        // TODO: handle exception
	        e.printStackTrace();
	    }
	    return sb.toString();
	}
}
