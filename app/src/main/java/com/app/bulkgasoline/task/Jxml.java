package com.app.bulkgasoline.task;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Jxml {
	private String m_strDoc;
	private Node rootNode = null;
	private Node headNode = null;
	private Node pointer = null;

	public Jxml() {
	}

	public boolean Load(String szFileName) {

		return true;
	}

	public boolean SetDoc(String strDoc) {
		try {
			SaxDoc sax = new SaxDoc();
			SAXParser sp;
			sp = SAXParserFactory.newInstance().newSAXParser();
			sp.parse(new InputSource(new StringReader(strDoc)), sax);

			headNode = sax.getRootNode();
			rootNode = new Node(true);
			rootNode.SetNext(headNode);
			headNode.SetPre(rootNode);
			pointer = rootNode;

			return true;

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
		}

		return false;
	}

	public boolean FindElem() {
		boolean result = false;
		if (pointer == null)
			return result;

		Node node = pointer.GetNext();
		if (node != null) {
			result = true;
			pointer = node;
		}
		return result;
	}

	public boolean FindElem(String szName) {

		boolean result = false;
		Node node = pointer.GetNext();
		while (node != null) {
			if (node.GetTagName().equalsIgnoreCase(szName)) {
				result = true;
				pointer = node;
				break;
			}
			node = node.GetNext();
		}
		return result;
	}

	public boolean IntoElem() {
		if (pointer == null)
			return false;

		pointer = pointer.GetChildernHead();
		return true;
	}

	public boolean OutOfElem() {
		if (pointer == null)
			return false;

		pointer = pointer.GetParent();
		return true;
	}

	public String GetTagName() {
		if (pointer == null)
			return "";

		return pointer.GetTagName();
	}

	public String GetData() {
		if (pointer == null)
			return "";

		return pointer.GetData();
	}

	public String GetElemContent() {

		return "";
	}

	public float GetFloatAttrib(String szAttrib) {
		if (pointer == null)
			return 0;
		return Float.parseFloat(pointer.GetAttribute(szAttrib));
	}

	public int GetIntAttrib(String szAttrib) {
		if (pointer == null)
			return 0;

		return Integer.parseInt(pointer.GetAttribute(szAttrib));
	}

	public String GetAttrib(String szAttrib) {
		if (pointer == null)
			return "";

		return pointer.GetAttribute(szAttrib);
	}

	// Create
	public boolean Save(String szFileName) {

		return true;
	}

	public String GetDoc() {
		return m_strDoc;
	}
};

class Node {
	private String strTagName = ""; // <TagName></TagName>
	private String strData = ""; // <![CDATA[ ]]>
	private Node _parent = null;
	private Node _next = null;
	private Node _pre = null;
	private Node _child_head = null;
	private boolean isheadnode = false;

	private Map<String, String> mapAttribute = new HashMap<String, String>();
	protected ArrayList<Node> _childern = new ArrayList<Node>();

	public Node() {
		_child_head = new Node(true);
		_childern.add(_child_head);

	}

	public Node(boolean headnode) {
		if (!headnode) {
			_child_head = new Node();
			return;
		}
		isheadnode = headnode;
	}

	public boolean IsHeadNode() {
		return isheadnode;
	}

	public void SetTagName(String tag) {
		strTagName = tag;
	}

	public String GetTagName() {
		return strTagName;
	}

	public void SetData(String data) {
		strData = data;
	}

	public String GetData() {
		return strData;
	}

	public void SetParent(Node parent) {
		_parent = parent;
	}

	public Node GetParent() {
		return _parent;
	}

	public Node GetNext() {
		return _next;
	}

	public void SetNext(Node next) {
		_next = next;
	}

	public Node GetPre() {
		return _pre;
	}

	public void SetPre(Node pre) {
		_pre = pre;
	}

	public Node GetChildernHead() {
		return _child_head;
	}

	public void SetAttribute(String name, String value) {
		mapAttribute.put(name, value);
	}

	public String GetAttribute(String name) {

		return mapAttribute.get(name);
	}

	public void AddChild(Node child) {
		Node tail = _childern.get(_childern.size() - 1);
		tail._next = child;
		child._pre = tail;
		child._next = null;
		child._parent = this;
		_childern.add(child);
	}
};

class SaxDoc extends DefaultHandler {

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		if (curElem != null) {
			curElem.SetData(curElem.GetData() + new String(ch, start, length));
		}

		super.characters(ch, start, length);
	}

	private Node topElem = null, curElem = null;

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		if (topElem == null) {
			topElem = CreateDataElem(localName);
			topElem.SetParent(null);

			for (int i = 0; i < attributes.getLength(); i++) {
				topElem.SetAttribute(attributes.getQName(i),
						attributes.getValue(i));
			}

			curElem = topElem;
		} else {
			Node tempElem;
			tempElem = CreateDataElem(localName);
			tempElem.SetParent(curElem);
			for (int i = 0; i < attributes.getLength(); i++) {
				tempElem.SetAttribute(attributes.getQName(i),
						attributes.getValue(i));
			}
			curElem.AddChild(tempElem);
			curElem = tempElem;
		}

		super.startElement(uri, localName, qName, attributes);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		if (curElem != null)
			curElem = curElem.GetParent();

		super.endElement(uri, localName, qName);
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}

	public Node getRootNode() {
		return topElem;
	}

	public Node CreateDataElem(String tagName) {
		Node elem = new Node();
		elem.SetTagName(tagName);
		return elem;
	}
}
