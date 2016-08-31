package Assignment03;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;


public class BooksGui  extends JPanel implements ListSelectionListener, ActionListener {
	private static final String JAXP_SCHEMA_LANGUAGE  = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	private static final String W3C_XML_SCHEMA  = "http://www.w3.org/2001/XMLSchema";
	private static DOMUtilities util = new DOMUtilities();
	private static JFrame frame;
	private JList<String> isbnList;
	private JTextArea text;
	private JPanel bookPanel;
	private BookSet books;
	private JMenuBar menuBar;
	private JMenu file, edit;
	private JMenuItem open, save, add, remove;
	private JFileChooser fc;
	private JSplitPane splitPane;
	private Document currentDocument;
	private static final int HEIGHT = 800;
	private static final int WIDTH = 800;
	private static final int LIST_WIDTH = 100;
	
	public BooksGui() {
		super(new BorderLayout());
		fc = new JFileChooser();
		menuBar = new JMenuBar();
		file = new JMenu("FILE");
		edit = new JMenu("EDIT");
		open = new JMenuItem("OPEN");
		save = new JMenuItem("SAVE");
		add = new JMenuItem("ADD");
		remove = new JMenuItem("REMOVE");
		save.addActionListener(this);
		open.addActionListener(this);
		add.addActionListener(this);
		remove.addActionListener(this);
		file.add(open);
		file.add(save);
		edit.add(add);
		edit.add(remove);
		
		menuBar.add(file);
		menuBar.add(edit);

		text = new JTextArea();
		text.setPreferredSize(new Dimension(WIDTH, 50));
		text.setBorder(BorderFactory.createEtchedBorder(1));
		bookPanel = new JPanel();
		bookPanel.setPreferredSize(new Dimension(WIDTH - LIST_WIDTH, 750));
		bookPanel.setBorder(BorderFactory.createBevelBorder(1));
		splitPane = new JSplitPane();
		isbnList = new JList<>();
		isbnList.setPreferredSize(new Dimension(LIST_WIDTH, 750));
		splitPane.setLeftComponent(isbnList);
		splitPane.setRightComponent(bookPanel);
		add(splitPane, BorderLayout.CENTER);
		add(text, BorderLayout.SOUTH);
		add(menuBar, BorderLayout.NORTH);

	}
	
	
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		String selected = isbnList.getSelectedValue();
		if(!e.getValueIsAdjusting()) {
			bookPanel.removeAll();
			if(books.getBook(selected) != null) {
				JComponent b = books.getBook(selected).preparePanel();
				b.setPreferredSize(new Dimension(600, 600));
				bookPanel.add(b);
				splitPane.setRightComponent(bookPanel);
			}
		}
		
	}
	public static void main(String[] args) {
		BooksGui gui = new BooksGui();
		frame = new JFrame("Book GUI");
		frame.setSize(WIDTH, HEIGHT);
		frame.setFocusable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(gui);
		//gets the dimensions for screen width and height to calculate center
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dimension = toolkit.getScreenSize();
		int screenHeight = dimension.height;
		int screenWidth = dimension.width;
		frame.pack(); //resize frame apropriately for its content
		//positions frame in center of screen
		frame.setLocation(new Point((screenWidth/2)-(frame.getWidth()/2),
				(screenHeight/2)-(frame.getHeight()/2)));
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source == open) {
			int val = fc.showOpenDialog(this);
			if (val == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            try {
	            	bookPanel.removeAll();
		            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		            builderFactory.setNamespaceAware(true);
		            builderFactory.setValidating(true);
		            builderFactory.setAttribute(JAXP_SCHEMA_LANGUAGE,W3C_XML_SCHEMA);
		            DocumentBuilder builder = builderFactory.newDocumentBuilder();
		            // parse the input stream
		            currentDocument = builder.parse(file);
		            books = new BookSet(currentDocument);
		            text.setText(books.getDescription());
		           	isbnList = new JList<>(books.getISBNListModel());
		           	isbnList.addListSelectionListener(this);
		           	isbnList.setPreferredSize(new Dimension(LIST_WIDTH, 750));
		            splitPane.setLeftComponent(isbnList);
		            frame.pack();
		        }
		        catch (FactoryConfigurationError ex) {
		            System.out.println(ex.getMessage());
		        }
		        catch (ParserConfigurationException ex) {
		            System.out.println(ex.getMessage());
		        }
		        catch (SAXException ex) {
		            System.out.println(ex.getMessage());
		        }
		        catch (IOException ex) {
		            System.out.println(ex.getMessage());
		        }
	        } 
			 
		} else if(source == save) {
			if(currentDocument == null) {
				JOptionPane.showMessageDialog(this, "There is no file to save!");
			} else {
				int val = fc.showSaveDialog(this);
				if (val == JFileChooser.APPROVE_OPTION) {
					try {
						DocumentBuilder build = DocumentBuilderFactory.newInstance().newDocumentBuilder();
						Document newDocument = build.newDocument();
						Node root = currentDocument.getDocumentElement();
						
						for(Node d : util.getAllChildNodes(root, "description")) {
							d.setTextContent(text.getText());
						}
						
						Element author, book, title, publisher;
						Iterator<String> authors;
						for(Node i : util.getAllChildNodes(root, "book")) {
							root.removeChild(i);
						}
						for(Book b : books) {
							book = currentDocument.createElement("book");
							
							// Set the three attributes
							if(b.getEdition() == 0) {
								throw new EditionException();
							}
							book.setAttribute("edition", String.valueOf(b.getEdition()));
							if(b.getCover() == Book.Cover.SOFT) {
								book.setAttribute("cover", "soft");
							} else book.setAttribute("cover", "hard");
							book.setAttribute("isbn", b.getISBN());
							
							// Add title node
							title = currentDocument.createElement("title");
							title.setTextContent(b.getTitle());
							book.appendChild(title);
							
							//Add author nodes
							authors = b.getAuthors();
							if(!authors.hasNext()) {
								throw new NullPointerException();
							}
							while(authors.hasNext()) {
								author = currentDocument.createElement("author");
								author.setTextContent(authors.next());
								book.appendChild(author);
							}
							
							//Add pub node
							publisher = currentDocument.createElement("pub");
							publisher.setAttribute("website", b.getPublisher().getWebSite());
							publisher.setAttribute("name", b.getPublisher().getName());
							book.appendChild(publisher);
							
							//Add book
							root.appendChild(book);
						}
						
						FileOutputStream output = new FileOutputStream(fc.getSelectedFile().getAbsolutePath());
						Transformer transformer = TransformerFactory.newInstance().newTransformer();
						transformer.setOutputProperty(OutputKeys.INDENT, "yes");
						transformer.transform(new DOMSource(currentDocument), new StreamResult(output));
						output.flush();
						output.close();
						
					} catch (TransformerException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (ParserConfigurationException e1) {
						e1.printStackTrace();
					} catch (NullPointerException e1) {
						JOptionPane.showMessageDialog(this, "You did not specify details for newly added book(s), please do so and save again");
					} catch (EditionException e1) {
						JOptionPane.showMessageDialog(this, "A book can not have the edition of '0', please fix this and save again");
					}
				}
			}
			
		} else if(source == add) {
			if(books == null) {
				JOptionPane.showMessageDialog(this, "Please open a xml file first");
			} else {
				String input = JOptionPane.showInputDialog(this, "Please enter ISBN:");
				if(input != null) {
					books.add(new Book(input));
				}
				
			}
			
				
		}  else if(source == remove) {
			if(books == null) {
				JOptionPane.showMessageDialog(this, "Please open a xml file first");
			} else
				if(isbnList.getSelectedValue() != null) {
					books.remove(books.getBook(isbnList.getSelectedValue()));
				} else {
					JOptionPane.showMessageDialog(this, "Please choose a book first");
				}
		}
	}
	private class EditionException extends Exception {}
}
