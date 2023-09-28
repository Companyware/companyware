/*******************************************************************************
 * MIT License
 *
 * Companyware - a java desktop framework for plugins
 *
 * Copyright (c) 2023 mbdus-Softwareentwicklung - Mathias Bauer
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package plugins.core.plugins.model;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import core.ApplicationContextProvider;
import core.TextMessages;

public class DisplayableObjectTableModel<T> extends ObjectTableModel<T> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<Integer, ColumnInfo> columnInfoMap;

    public DisplayableObjectTableModel(Class<T> tClass) {
        init(tClass);
    }

    private void init(Class<T> tClass) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(tClass);
            this.columnInfoMap = new HashMap<>();
            for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
                Method m = pd.getReadMethod();
                DisplayAs displayAs = m.getAnnotation(DisplayAs.class);
                if (displayAs == null) {
                    continue;
                }
                ColumnInfo columnInfo = new ColumnInfo();
                columnInfo.displayName = displayAs.value();
                columnInfo.index = displayAs.index();
                columnInfo.editable = displayAs.editable();
                if (displayAs.editable()) {
                    columnInfo.setterMethod = pd.getWriteMethod();
                }
                columnInfo.getterMethod = m;
                columnInfo.propertyName = pd.getName();
                columnInfoMap.put(columnInfo.index, columnInfo);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getValueAt(T t, int columnIndex) {
        try {
            return columnInfoMap.get(columnIndex)
                    .getterMethod.invoke(t);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getColumnCount() {
        return columnInfoMap.size();
    }

    @Override
    public String getColumnName(int column) {
        ColumnInfo columnInfo = columnInfoMap.get(column);
        if (columnInfo == null) {
            throw new RuntimeException("No column found for index " + column);
        }
        if(!columnInfo.displayName.equals("")){
        	TextMessages service = ApplicationContextProvider.getContext().getBean(TextMessages.class);
        	return service.get(columnInfo.displayName);
        }
        return columnInfo.displayName;
    }
    public Class<?> getColumnClass(int columnIndex) {
        ColumnInfo columnInfo = columnInfoMap.get(columnIndex);
        return columnInfo.getterMethod.getReturnType();
    }

    @Override
    public String getFieldName(int column) {
        ColumnInfo columnInfo = columnInfoMap.get(column);
        return columnInfo.propertyName;
    }

    @Override
    public boolean isColumnEditable(int columnIndex) {
        ColumnInfo columnInfo = columnInfoMap.get(columnIndex);
        return columnInfo.editable;
    }

    @Override
    public boolean setObjectFieldValue(T t, int column, Object value) {
        ColumnInfo columnInfo = columnInfoMap.get(column);
        try {
            if (columnInfo.setterMethod != null 
            	&& !columnInfo.displayName.equals("plugin.action")
            	&& !columnInfo.displayName.equals("plugin.delete")
            	&& !columnInfo.displayName.equals("")) {
                columnInfo.setterMethod.invoke(t, value);
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    private static class ColumnInfo {
        private Method setterMethod;
        private Method getterMethod;
        private int index;
        private String displayName;
        private String propertyName;
        private boolean editable;
    }
}