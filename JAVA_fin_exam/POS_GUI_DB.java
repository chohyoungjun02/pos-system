package JAVA_fin_exam;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class POS_GUI_DB extends JFrame implements ActionListener{

	String[] dbFieldName = {"menu","price","barcord","stock"};
	public JLabel[] label = new JLabel[dbFieldName.length];
	public JTextField[] tf = new JTextField[dbFieldName.length];
	String[] btnText = {"insert","update","delete","clear","pos","reload"};
	public JButton[] btn = new JButton[btnText.length];
	
	Vector<String> header = new Vector<>();
	DefaultTableModel model;
	JTable table;
	Vector<Vector<String>> data = new Vector<>();
	String tableName = "POSmenuTable_A_조형준";
	Connection con;
	
	public POS_GUI_DB() {
		
		con = ItemDBConnect.makeConnection();
		ItemDBConnect.dropTable(con, tableName);
		ItemDBConnect.createTable(con, tableName);
		
		Item item1 = new Item("신라면",4000,"0 123 1234",10);
		Item item2 = new Item("커피",2000,"0 321 1234",10);
		Item item3 = new Item("콜라",1500,"0 231 1234",10);
		
		ItemDBConnect.insertIntoTable(con, tableName, item1);
		ItemDBConnect.insertIntoTable(con, tableName, item2);
		ItemDBConnect.insertIntoTable(con, tableName, item3);
		
		data = selectDB(con, tableName);  // 다음시간에....
		
		this.setTitle("POS_GUI : 202155026 조형준");
		this.setSize(750,300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		createMenu();
		
		JPanel pan = new JPanel();
		pan.setLayout(new GridLayout(0,2));
		for(int i=0;i<label.length;i++) {
			label[i] = new JLabel(dbFieldName[i],JLabel.CENTER);
			tf[i] = new JTextField(15);
			pan.add(label[i]);
			pan.add(tf[i]);
		}
		for(int i=0;i<btn.length;i++) {
			btn[i] = new JButton(btnText[i]);
			btn[i].addActionListener(this);
			pan.add(btn[i]);
		}
		this.add(pan,BorderLayout.CENTER);
		
		for(int i=0;i<dbFieldName.length;i++) {
			header.add(dbFieldName[i]);
		}
		model = new DefaultTableModel(header,0);
		table = new JTable(model);
		model.setDataVector(data, header);
		JScrollPane scrollpane = new JScrollPane(table);
		this.add(scrollpane, BorderLayout.EAST);
		
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if (e.getSource() == table) {
					int rowIndex = table.getSelectedRow(); // 어디를 클릭했느닞 확인
					Vector<String> row = new Vector<>();
					row = data.get(rowIndex);
					for (int i = 0; i<tf.length; i++) {
						tf[i].setText(row.get(i)); //tf에 클릭한 정보를 출력
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		this.pack();
		this.setVisible(true);
	}
	private void createMenu() {
		JMenuBar mb = new JMenuBar();
		JMenu fileMenu = new JMenu("file");
		
		JMenuItem open = new JMenuItem("open");
		JMenuItem save = new JMenuItem("save");
		JMenuItem saveAs = new JMenuItem("saveAs");
		JMenuItem exit = new JMenuItem("exit");
		
		fileMenu.addSeparator();
		fileMenu.add(open);
		fileMenu.add(save);
		fileMenu.add(saveAs);
		fileMenu.add(exit);
		
		open.addActionListener(this);
		save.addActionListener(this);
		saveAs.addActionListener(this);
		exit.addActionListener(this);
		
		mb.add(fileMenu);
		this.setJMenuBar(mb);
		
	}
	private Vector<Vector<String>> selectDB(Connection con2, String tableName2) {
		this.data.clear(); // clear 없으면 테이블이 중복되어서 출력됨
		String selectSQL = "SELECT * FROM "+ tableName + ";";
		try {
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()){
				Vector<String> row = new Vector<>();
				row.add(rs.getString(1));
				row.add(""+rs.getInt("price"));
				row.add(rs.getString(3));
				row.add(""+rs.getInt("stock"));
				data.add(row);
			}
			} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.data;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btn[0]) {
			Item item1 = new Item(tf[0].getText(),Integer.parseInt(tf[1].getText()),tf[2].getText(),Integer.parseInt(tf[3].getText()));
			ItemDBConnect.insertIntoTable(con, tableName, item1);
			data = selectDB(con,tableName);
		}
		if(e.getActionCommand() == "update") {
			Item item = new Item(tf[0].getText(), Integer.parseInt(tf[1].getText()),tf[2].getText(),Integer.parseInt(tf[3].getText()));
			ItemDBConnect.updateTable(con, tableName, item);
			data = selectDB(con, tableName);
			model.setDataVector(data, header);
			table.repaint();
		}
		if(e.getActionCommand() == "delete") {
			String menu = tf[0].getText(); 
	        ItemDBConnect.deleteFromTable(con, tableName, menu);
	        data = selectDB(con,tableName);
		}
		if(e.getActionCommand() == "clear") {
			for(int i=0;i<tf.length;i++) {
				tf[i].setText("");
				}
			data = selectDB(con,tableName);
		}
		if(e.getActionCommand() == "pos") {
			Order_GUI og = new Order_GUI(data);
		}
		if(e.getActionCommand() == "reload") {
			data = selectDB(con, tableName);
	        model.setDataVector(data, header);
	        table.repaint();
	        JOptionPane.showMessageDialog(this, "재고를 새로고침했습니다!", "Reload", JOptionPane.INFORMATION_MESSAGE);
		}
		//메뉴바
	    if(e.getActionCommand() ==("open")) {
	        System.out.println("Open 메뉴 선택됨");
	        java.util.Date now = new java.util.Date(); 
	        javax.swing.JOptionPane.showMessageDialog(
	            this, 
	            "개점했습니다!\n현재 날짜: " + now.toString(), 
	            "Open", 
	            javax.swing.JOptionPane.INFORMATION_MESSAGE
	        );
	        new Order_GUI(data);
	    }
	    if(e.getActionCommand() ==("save")) {
	        System.out.println("Save 메뉴 선택됨");
	        // 저장 로직 추가 가능
	    }
	    if(e.getActionCommand() ==("saveAs")) {
	        System.out.println("SaveAs 메뉴 선택됨");
	        // 다른 이름으로 저장 로직 추가 가능
	    }		
	    if(e.getActionCommand() ==("exit")) {
	        java.util.Date now = new java.util.Date(); 
	        javax.swing.JOptionPane.showMessageDialog(
	            this, 
	            "마감했습니다!\n현재 날짜: " + now.toString(), 
	            "Close", 
	            javax.swing.JOptionPane.INFORMATION_MESSAGE
	        );
	        System.exit(0);
	    }		
		
		model.setDataVector(data,header);
		table.repaint();
		
	}
}
