/*
 * Copyright (c) 2011-2020, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.smallbun.framework.injector.methods;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.smallbun.framework.injector.AbstractDataScopeLogicMethod;

/**
 * <p>
 * 根据 ID 集合查询
 * </p>
 *
 * @author hubin
 * @since 2018-06-13
 */
public class DataScopeLogicSelectBatchByIds extends AbstractDataScopeLogicMethod {

	@Override
	public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
		SqlMethod sqlMethod = SqlMethod.LOGIC_SELECT_BATCH_BY_IDS;
		SqlSource sqlSource = languageDriver.createSqlSource(configuration,
				String.format(sqlMethod.getSql(), sqlSelectColumns(tableInfo, false), tableInfo.getTableName(),
						tableInfo.getKeyColumn(),
						SqlScriptUtils.convertForeach("#{item}", COLLECTION, null, "item", COMMA),
						tableInfo.getLogicDeleteSql(true, false)), Object.class);
		return addSelectMappedStatement(mapperClass, sqlMethod.getMethod(), sqlSource, modelClass, tableInfo);
	}
}
