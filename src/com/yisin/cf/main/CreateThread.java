package com.yisin.cf.main;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.io.File;
import java.util.List;

import javax.swing.JOptionPane;

import com.yisin.cf.db.DbCache;
import com.yisin.cf.entity.CreAttr;
import com.yisin.cf.entity.DbTableColumn;
import com.yisin.cf.form.MainWindow;
import com.yisin.cf.util.Utililies;

public class CreateThread extends Thread {
	List<String> tables = null;
	CreAttr ca = null;

	public CreateThread(List<String> tables, CreAttr ca) {
		this.tables = tables;
		this.ca = ca;
	}

	public void run() {
	    MainWindow.mainWindow.showProcessPanel();
		if(tables.size() == 0){
			tables.add(Utililies.entityToTable(ca.getEntityName()));
		}
		List<DbTableColumn> columnList = null;
		String tableName = null;
		CreAttr creAtt = null;
		for (int i = 0, k = tables.size(); i < k; i++) {
			tableName = tables.get(i);
			columnList = DbCache.mysqlDbColumnMap.get(tableName);
			creAtt = ca.clone();
			new Creater(creAtt, tableName, columnList).run();
		}
		// 执行完成
		MainWindow.mainWindow.hideProcessPanel();
		Toolkit.getDefaultToolkit().beep();
        JOptionPane.showMessageDialog(null, "恭喜，生成完成\n路径：" + ca.getSaveDir(), "提示", JOptionPane.ERROR_MESSAGE);
        try {
            Desktop.getDesktop().open(new File(ca.getSaveDir())); 
        } catch (Exception e) {
        }
	}

}
