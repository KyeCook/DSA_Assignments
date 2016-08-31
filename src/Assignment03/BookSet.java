package Assignment03; /******************************************************************************************
PROGRAM NAME:   ASSIGNMENT 3: BOOK SET PROGRAM
FILE:           BookSet.java
DESCRIPTION:    Holds a collection of books in a hash map with isbn codes as keys maintained
                in the order which books are added to the collection
*******************************************************************************************/
import javax.swing.ListModel;
import java.util.AbstractSet;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.NamedNodeMap;
import javax.swing.AbstractListModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.io.IOException;


public class BookSet extends AbstractSet<Book> {
	private DOMUtilities util = new DOMUtilities();
    private LinkedHashMap<String,Book> booksMap;
    private ISBNListModel isbnListModel;
    private String description;
    private static final String JAXP_SCHEMA_LANGUAGE  = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    private static final String W3C_XML_SCHEMA  = "http://www.w3.org/2001/XMLSchema";

    //default constructor that initialises LinkedHashMap and call to super class
    public BookSet() {
        super();
        booksMap = new LinkedHashMap<>();
        isbnListModel = null;
    }
    //constructor that will accept a Collection of Books
    public BookSet(Collection<? extends Book> books) {
    	this();
    	for(Book i : books) {
    		booksMap.put(i.getISBN(), i);
    	}
         //complete this
    }
    //constructor that will accept a Document that holds XML information
    public BookSet(Document document) {
    	this();
        //complete this method by traversing the XML tree, start from the root node
        //and look for books
    	document.getDocumentElement().normalize();
		Node rootXMLNode = document.getDocumentElement();
		Collection<Node> books = util.getAllChildNodes(rootXMLNode, "book");
		for(Node z : util.getAllChildNodes(rootXMLNode, "description")) {
			setDescription(util.getTextContent(z));
		}
		
		String isbn, title = "";
		int edition;
		Publisher pub = null;
		Book.Cover cover;
		Collection<String> authors = new ArrayList<>();
		for(Node i : books) {
			isbn = util.getAttributeString(i, "isbn");
			edition = Integer.parseInt(util.getAttributeString(i, "edition"));
			if(util.getAttributeString(i, "cover").toLowerCase().equals("soft")) {
				cover = Book.Cover.SOFT;
			} else 
				cover = Book.Cover.HARD;
			for(Node j : util.getAllChildNodes(i, "title")) {
				title = util.getTextContent(j);
			}
			authors.clear();
			for(Node a : util.getAllChildNodes(i, "author")) {
				authors.add(util.getTextContent(a));
			}
			for(Node b : util.getAllChildNodes(i, "pub")) {
				pub = new Publisher(util.getAttributeString(b, "name"), util.getAttributeString(b, "website"));
			}
			booksMap.put(isbn, new Book(isbn, title, authors.iterator(), pub, cover, edition));
		}
		
    }
    //this method adds a Book to the hashmap if is doesn't already contain
    //that isbn number and also adds the isbn value to the isbnListModel
    public boolean add(Book book) {   	
    	if(booksMap.putIfAbsent(book.getISBN(), book) == null) {
    		isbnListModel.addISBN(book.getISBN());
    		return true;
    	} return false;
    }
    //this method removes a Book from the hashmap if it contains
    //that isbn number and also removes the isbn value from the isbnListModel
    public boolean remove(Object o) {
    	Book b = (Book) o;
    	if(booksMap.remove(b.getISBN()) != null) {
    		isbnListModel.removeISBN(b.getISBN());
    		return true;
    	}
    	return false;
    }
    //returns an iterator to iterate through the list
    public Iterator<Book> iterator()
    {    //dont change
        return booksMap.values().iterator();
    }
    //returns the number of values in the hashmap
    public int size() {
        return booksMap.size();
    }
    //clears the hashmap
    public void clear() {
    	booksMap.clear();
    }
    //returns the book from hashmap without removing it with
    //the neccessary isbn number
    public Book getBook(String isbn) {
    	return booksMap.get(isbn);
    }
    //returns a ListModel with isbn values
    public ListModel<String> getISBNListModel() {
    	if(isbnListModel == null) {
    		isbnListModel = new ISBNListModel(booksMap.keySet());
    	}
    	return isbnListModel;
    }
    //returns description of XML file
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description.trim();
    }
    //string representation of XML file
    public String toString()
    {   //dont change
        Iterator<Book> iterator = iterator();
        String data = "";
        while(iterator.hasNext())
        {
            Book book = iterator.next();
            data += book.toString()+"\n";
        }
        return data;
    }

    // This method returns all the child Nodes of the parentNode
    // that has a given NodeName (case insensitive)
    private Collection<Node> getAllChildNodes(Node parentNode, String name)
    {   //dont change
        ArrayList<Node> nodeList = new ArrayList<>();
        NodeList childNodes = parentNode.getChildNodes();
        if(name != null)
        {
            for (int i=0; i<childNodes.getLength(); i++)
            {
                Node childNode = childNodes.item(i);
                if(name.equalsIgnoreCase(childNode.getNodeName()))
                    nodeList.add(childNode);
            }
        }
        return nodeList;
    }
    // This method returns the attribute of the parentNode as a String
    // that has a given NodeName (case sensitive!) or null if no such
    // attribute is found
    private String getAttributeString(Node parentNode, String name)
    {  //dont change
        String attribute = null;
        if((parentNode != null) && (name != null))
        {
            NamedNodeMap attributeNodes = parentNode.getAttributes();
            if(attributeNodes != null)
            {
                Node attributeNode = attributeNodes.getNamedItem(name);
                if(attributeNode != null)
                    attribute = attributeNode.getNodeValue();
            }
        }
        return attribute;
    }
    // This method returns the text content of a node which should
    // occur as Text child nodes of the node that have name "#text"
    // Note there should actually only be a single text node if DOM
    // tree has been normalized
    private String getTextContent(Node node)
    {   //dont change
        String textContent = "";
        if(node != null)
        {
            NodeList childNodes = node.getChildNodes();
            for(int i=0; i<childNodes.getLength(); i++)
            {
                Node childNode = childNodes.item(i);
                if(childNode instanceof Text)
                    textContent += childNode.getNodeValue();
            }
        }
        return textContent;
    }
    //inner class that holds a ArrayList of strings that relate to
    //the isbn keys for the HashMap used for the Gui JList
    //DONT CHANGE THIS CLASS
    private class ISBNListModel extends AbstractListModel {
		private static final long serialVersionUID = 1L;
		private ArrayList<String> isbnList;

        //constructor initialises arrayList and sorts the collection out
        //in its natural order
        public ISBNListModel(Collection<String> isbnData)
        {
            super();
            isbnList = new ArrayList<>();
            isbnList.addAll(isbnData);
            Collections.sort(isbnList);
        }
        //gets the element at the specified index
        public Object getElementAt(int index)
        {
            if(index < isbnList.size())
                return isbnList.get(index);
            else
                return null;
        }
        //gets the size of the arrayList
        public int getSize()
        {
            return isbnList.size();
        }
        //adds to the isbn to the arrayList and notifies
        //any listeners to it by fireIntervalAdded
        public boolean addISBN(String isbn)
        {
            int index = Collections.binarySearch(isbnList,isbn);
            index = (index * -1)-1;
            isbnList.add(index,isbn);
            fireIntervalAdded(this,index,index);
            return true;
        }
        //removes the isbn from the arrayList and notifies
        //any listeners to it by fireIntervalRemoved
        public boolean removeISBN(String isbn)
        {
            int index = isbnList.indexOf(isbn);
            isbnList.remove(isbn);
            fireIntervalRemoved(this,index,index);
            return true;
        }
    }
    //main method to test the program
    public static void main(String args[])
    {
        System.out.println("==============BOOK SET===============");
        Collection<String> authors = new ArrayList<>();
        authors.add("Seth Hall");
        authors.add("Bob Chen");
        Book book1 = new Book("1234","JAVA DEVELOPMENT",authors.iterator());
        Book book2 = new Book("1111","Lunch Time",authors.iterator());
        Collection<Book> coll = new ArrayList<>();
        coll.add(book1);
        coll.add(book2);
        
        System.out.println("TEST WITH COLLECTION CONSTRUCTOR: ");
        BookSet bookset2 = new BookSet(coll);
        System.out.println(bookset2);
        
        System.out.println("TEST WITH DOCUMENT CONSTRUCTOR: ");
        String file = "src/Assignment03/ComputerBooks.xml";
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            builderFactory.setNamespaceAware(true);
            builderFactory.setValidating(true);
            builderFactory.setAttribute(JAXP_SCHEMA_LANGUAGE,W3C_XML_SCHEMA);
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            // parse the input stream
            Document document = builder.parse(file);
            BookSet bookset = new BookSet(document);
            System.out.println(bookset);

        }
        catch (FactoryConfigurationError | ParserConfigurationException | SAXException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
