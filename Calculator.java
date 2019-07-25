import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
class Calculator extends JFrame implements ActionListener{
	JLabel lblHistory,lblCurrent;
	Font f=new Font(Font.SERIF,Font.BOLD,20);
	double historyValue,currentValue,memory,result;
	char currentOperator;
	Color c=new Color(230,230,230);
	String strMemoryButtons[]={"MC","MR","M+","M-","MS"};
	String strOtherButtons[]={"%","\u221A","x\u00B2"," 1/X",
	"CE","C","\u232B","/","7","8","9","*",
	"4","5","6","+","1","2","3","-",
	"\u00B1","0",".","="};
	JButton btnMemory[],btnOthers[];
	JPanel panTop,panBottom,panMemory;
	boolean flagConcat=true;
	Calculator(){
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("CALCULATOR");
		
		//Memory Panel
		panMemory=new JPanel();
		panMemory.setLayout(new GridLayout(1,5));
		btnMemory=new JButton[5];
		for(int i=0;i<5;i++){
			btnMemory[i]=new JButton(strMemoryButtons[i]);
			btnMemory[i].addActionListener(this);
			btnMemory[i].setFocusPainted(false);
			btnMemory[i].setBorderPainted(false);
			btnMemory[i].addMouseListener(new MouseAdapter(){
				public void mouseEntered(MouseEvent me){
					JButton b=(JButton)me.getSource();
					b.setBackground(Color.LIGHT_GRAY);
				}
				public void mouseExited(MouseEvent me){
					JButton b=(JButton)me.getSource();
 					b.setBackground(c);
				}		
			});
			btnMemory[i].setBackground(c);
			btnMemory[i].setFont(f);
			btnMemory[i].setForeground(Color.black);
			panMemory.add(btnMemory[i]);
		}

		//Top Panel
		panTop=new JPanel();
		panTop.setLayout(new GridLayout(3,1));
		lblHistory=new JLabel("",SwingConstants.RIGHT);
		lblHistory.setFont(new Font(Font.SERIF,Font.BOLD,24));
		lblCurrent=new JLabel("0",SwingConstants.RIGHT);
		lblCurrent.setFont(new Font(Font.SERIF,Font.BOLD,40));
		panTop.add(lblHistory);
		panTop.add(lblCurrent);
		panTop.add(panMemory);
		add(panTop,BorderLayout.NORTH);

		//Other Buttons Panel
		panBottom=new JPanel();
		panBottom.setLayout(new GridLayout(6,4));
		btnOthers=new JButton[24];
		for(int i=0;i<24;i++){
			btnOthers[i]=new JButton(strOtherButtons[i]);
			btnOthers[i].setBorder(BorderFactory.createLineBorder(new Color(240,240,240)));
			btnOthers[i].setFocusPainted(false);
			if(Character.isDigit(strOtherButtons[i].charAt(0)))
	 			btnOthers[i].setBackground(Color.white);
	 		else
	 			btnOthers[i].setBackground(c);
			btnOthers[i].setForeground(Color.black);
			btnOthers[i].setFont(f);
			btnOthers[i].addActionListener(this);
			btnOthers[i].addMouseListener(new MouseAdapter(){
				public void mouseEntered(MouseEvent me){
					JButton b=(JButton)me.getSource();
					String str=b.getText();
					if(isOperator(str.charAt(0)) ||str.charAt(0)=='=' )
						b.setBackground(Color.blue);
					else
						b.setBackground(Color.LIGHT_GRAY);
				}
				public void mouseExited(MouseEvent me){
					JButton b=(JButton)me.getSource();
					String str=b.getText();
					if(Character.isDigit(str.charAt(0)))
	 					b.setBackground(Color.white);
	 				else
	 					b.setBackground(c);
				}	
			});
			panBottom.add(btnOthers[i]);
		}
		setFocusable(true);
		addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent ke){
				char ch=ke.getKeyChar();
				if(Character.isDigit(ch) || isOperator(ch)){
					for(int i=0;i<24;i++){
						if(strOtherButtons[i].charAt(0)==ch)
							btnOthers[i].doClick();
					}
				}
				else if(ch==KeyEvent.VK_ENTER){
					btnOthers[23].doClick();
				}
				else if(ch==KeyEvent.VK_ESCAPE){
					btnOthers[4].doClick();
				}
				else if(ch==KeyEvent.VK_BACK_SPACE){
					btnOthers[6].doClick();
				}
			}
		});
		add(panBottom);				
		setSize(400,500);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	public static void main(String args[]){
		new Calculator();
	}
	boolean isOperator(char ch){
		if(ch=='+' || ch=='-' ||ch=='*' ||ch=='/')
			return true;
		return false;
	}
	void setCurrent(String str){
		if(str.isEmpty())
			str="0";
		double n=Double.parseDouble(str);
		if(n==(int)n)
			str=(int)n+"";
		else
			str=n+"";
		lblCurrent.setText(str);
	}
	String format(String str){
		if(str.isEmpty())
			return "0";
		double n=Double.parseDouble(str);
		if(n==(int)n)
			return (int)n+"";
		else
			return n+"";
	}	
	public void actionPerformed(ActionEvent ae){
		JButton b=(JButton)ae.getSource();
		String str=b.getLabel();
		currentValue=Double.parseDouble(lblCurrent.getText());
		if(Character.isDigit(str.charAt(0))){
			if(flagConcat)//concat
				setCurrent(lblCurrent.getText()+str.charAt(0));
			else{//overlap
				setCurrent(str);
				flagConcat=true;
			}
		}
		else if(isOperator(str.charAt(0))){
			flagConcat=false;
			if(currentOperator!='\u0000'){
				switch(currentOperator){
					case '+':
						result=historyValue + currentValue;
						break;
					case '-':
						result=historyValue - currentValue;
						break;
					case '*':
						result=historyValue * currentValue;
						break;
					case '/':
						result=historyValue / currentValue;
						break;		
				}
				setCurrent(result+"");
				historyValue=result;
			}
			else
				historyValue=currentValue;	
			currentOperator=str.charAt(0);
			lblHistory.setText(lblHistory.getText()+format(currentValue+"")+currentOperator);
			
		}				
		else if(str.equalsIgnoreCase("=")){
			currentValue=Double.parseDouble(lblCurrent.getText());			
			switch(currentOperator){
				case '+':
					currentValue=historyValue + currentValue;
					break;
				case '-':
					currentValue=historyValue - currentValue;
					break;
				case '*':
					currentValue=historyValue * currentValue;
					break;
				case '/':
					currentValue=historyValue / currentValue;
					break;		
			}
			setCurrent(currentValue+"");
			historyValue=0;
			currentOperator='\u0000';
			lblHistory.setText("");
			flagConcat=false;
		}
		else if(str.equalsIgnoreCase("mc")){
			memory=0;
			flagConcat=false;
		}
		else if(str.equalsIgnoreCase("mr")){
			setCurrent(memory+"");
			flagConcat=false;
		}
		else if(str.equalsIgnoreCase("m+")){
			memory+=currentValue;
			flagConcat=false;
		}
		else if(str.equalsIgnoreCase("m-")){
			memory-=currentValue;	
			flagConcat=false;
		}
		else if(str.equalsIgnoreCase("ms")){
			memory=currentValue;			
			flagConcat=false;
		}
		else if(str.equalsIgnoreCase("%")){
			result=historyValue*currentValue/100;
			setCurrent(result+"");
			lblHistory.setText(lblHistory.getText()+format(result+""));
			flagConcat=false;
		}
		else if(str.equalsIgnoreCase("\u221A")){
			setCurrent(Math.sqrt(currentValue)+"");
			flagConcat=false;
		}
		else if(str.equalsIgnoreCase("x\u00B2")){
			setCurrent(currentValue*currentValue+"");
			flagConcat=false;	
		}
		else if(str.equalsIgnoreCase(" 1/x")){
			setCurrent(1/currentValue+"");
			flagConcat=false;
		}
		else if(str.equalsIgnoreCase("ce")){
			lblCurrent.setText("0");	
			flagConcat=false;
		}
		else if(str.equalsIgnoreCase("c")){
			lblHistory.setText("");
			lblCurrent.setText("0");
			currentValue=0;
			historyValue=0;
			currentOperator='\u0000';
			flagConcat=false;
		}
		else if(str.equalsIgnoreCase("\u232B")){
			setCurrent(lblCurrent.getText().substring(0,lblCurrent.getText().length()-1));
			flagConcat=false;
		}
		else if(str.equalsIgnoreCase("\u00B1")){
			setCurrent(-currentValue+"");
			flagConcat=false;	
		}
		else if(str.equalsIgnoreCase(".")){
			if(lblCurrent.getText().indexOf(".")== -1)
				lblCurrent.setText(lblCurrent.getText()+".");
			flagConcat=false;
		}
	}
}