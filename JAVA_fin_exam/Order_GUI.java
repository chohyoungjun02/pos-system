package JAVA_fin_exam;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class Order_GUI extends JFrame implements ActionListener, MouseListener {

	String[] orderTableName = {"menu", "price", "count","stock"};
	Vector<String> header = new Vector<>();
	DefaultTableModel model;
	
	public JPanel menuPanel = new JPanel();
	
	public JPanel btnPanel = new JPanel();
	String[] btnText = {"Order", "Cancel", "Payment"};
	JButton[] orderBtn = new JButton[btnText.length];
	JLabel label = new JLabel("금액");
	JTextField totalSumTF = new JTextField(10);
	
	public JTable order_table;
	Vector<Vector<String>> order_data = new Vector<>();
	Vector<Vector<String>> data = new Vector<>();
	JButton[] menuBtn; //선언만..
	String tableName = "POSmenuTable_A_조형준";
	Connection con;
	static int selectedRow = -1;
	
	public Order_GUI(Vector<Vector<String>> data2) {
		this.setTitle("Order_GUI:홍길동");
		this.setSize(450, 300);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		this.data = data2;
		
		// table
		for (int i=0; i<orderTableName.length; i++) {
			header.add(orderTableName[i]);
		}
		model = new DefaultTableModel(header, 0);
		model.setDataVector(order_data, header);
		order_table = new JTable(model);
		order_table.addMouseListener(this);
		JScrollPane scrollPane = new JScrollPane(order_table);
		this.add(scrollPane, BorderLayout.CENTER);
		
		// btnPanel
		for(int i=0; i<orderBtn.length; i++) {
			orderBtn[i] = new JButton(btnText[i]);
			btnPanel.add(orderBtn[i]);
			orderBtn[i].addActionListener(this);
		}
 		btnPanel.add(label);
 		btnPanel.add(totalSumTF);
		this.add(btnPanel, BorderLayout.SOUTH);
		
		
		// menuPanel : 다음시간에 설명...
		menuPanel.setLayout(new GridLayout(0,2));
		TitledBorder menuTitle = BorderFactory.createTitledBorder("POS_Menu");
		menuPanel.setBorder(menuTitle);
		
		menuBtn = new JButton[data.size()];
		for (int i=0; i<menuBtn.length;i++) {
			Vector<String> row = new Vector<>();
			row = data.get(i);
			menuBtn[i] = new JButton(row.get(0)+":"+row.get(1));;
			menuPanel.add(menuBtn[i]);
			menuBtn[i].addActionListener(this);
		}
 		this.add(menuPanel, BorderLayout.EAST);
		this.setVisible(true);
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		 for(int i=0;i<menuBtn.length;i++) {
	            if (e.getSource() == menuBtn[i]) {
	                Vector<String> row = data.get(i);
	                int currentStock = Integer.parseInt(row.get(3));

	                if(currentStock > 0) {
	                    // 재고 감소
	                    currentStock--;
	                    row.set(3, String.valueOf(currentStock));

	                    // 주문 테이블에 추가
	                    boolean isAddedItem = false;
	                    
	                    for(int j=0;j<order_data.size();j++) {
	                        Vector<String> currentRow = order_data.get(j);
	                        if(currentRow.get(0).equals(row.get(0))) {
	                            isAddedItem = true;
	                            int currentCount = Integer.parseInt(currentRow.get(2));
	                            currentRow.set(2, String.valueOf(currentCount+1));
	                            currentRow.set(3, row.get(3)); // stock 갱신
	                        }
	                    }
	                    if(!isAddedItem) {
	                        Vector<String> newRow = new Vector<>();
	                        newRow.add(row.get(0)); // menu
	                        newRow.add(row.get(1)); // price
	                        newRow.add("1");        // count
	                        newRow.add(row.get(3)); // stock
	                        order_data.add(newRow);
	                    }
	                } 
	                else {
	                    JOptionPane.showMessageDialog(this, "재고가 부족합니다!", "재고 부족", JOptionPane.WARNING_MESSAGE);
	                }
	            }
	        }

			if (e.getSource() == orderBtn[0]) { //order 버튼
				int totalSum = 0;
				for(int j=0;j<order_data.size();j++) {
					Vector<String> row = new Vector<>();
					row = order_data.get(j);
					int price = Integer.parseInt(row.get(1));
					int count = Integer.parseInt(row.get(2));
					totalSum += price*count;
				}
				totalSumTF.setText(String.valueOf(totalSum));
			}
			if(e.getSource() == orderBtn[1]) { // cancel 버튼
				if(selectedRow >= 0) { // 선택된 행이 있을 때
					System.out.println("SelectedRow:" + selectedRow);
			    	order_data.remove(selectedRow);
			    }
			    else { // 선택된 행이 없을 때 → 전체 삭제
			        order_data.clear();
			    }
			    model.setDataVector(order_data, header);
			    order_table.repaint();
			    totalSumTF.setText("");
			    selectedRow = -1; // 초기화
			}
			if(e.getSource()==orderBtn[2]) {//결제 버튼
				JFrame payFrame = new JFrame("결제창");
				payFrame.setSize(300,200);
				payFrame.setLayout(new GridLayout(0,1));
				JLabel creditlabel = new JLabel("카드로 결제");
				JButton okbtn = new JButton("결제"); //카드 결제 버튼
				JLabel label = new JLabel("현금을 입력하세요.");
				JTextField cashField = new JTextField(10);
				JButton okBtn = new JButton("결제"); //현금 결제 버튼
				
				payFrame.add(creditlabel);
				payFrame.add(okbtn);
				payFrame.add(label);
				payFrame.add(cashField);
				payFrame.add(okBtn);
				
				// 카드 결제 버튼 클릭 시 바로 완료 메시지 띄우기
				okbtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(
				                payFrame,
				                "결제가 완료되었습니다.",
				                "결제 성공",
				                JOptionPane.INFORMATION_MESSAGE
				            );
							payFrame.dispose(); // 결제창 닫기
				        }
				});

				    // 현금 결제 버튼 클릭 시
				    okBtn.addActionListener(new ActionListener() {
				        @Override
				        public void actionPerformed(ActionEvent e) {
				            try {
				                int cash = Integer.parseInt(cashField.getText());
				                int total = Integer.parseInt(totalSumTF.getText());

				                if (cash >= total) {
				                    int change = cash - total;
				                    JOptionPane.showMessageDialog(
				                        payFrame,
				                        "결제가 완료되었습니다. 거스름돈은 " + change + "원입니다.",
				                        "결제 성공",
				                        JOptionPane.INFORMATION_MESSAGE
				                    );
				                    
				                    payFrame.dispose();
				                } else {
				                    JOptionPane.showMessageDialog(
				                        payFrame,
				                        "금액이 부족합니다. 결제를 다시 시도해주세요.",
				                        "결제 실패",
				                        JOptionPane.ERROR_MESSAGE
				                    );
				                }
				            } catch (NumberFormatException ex) {
				                JOptionPane.showMessageDialog(
				                    payFrame,
				                    "유효한 숫자를 입력해주세요.",
				                    "입력 오류",
				                    JOptionPane.ERROR_MESSAGE
				                );
				            }
				        }
				    });
				payFrame.setVisible(true);
			}		
		}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		selectedRow = order_table.getSelectedRow();
		
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
}