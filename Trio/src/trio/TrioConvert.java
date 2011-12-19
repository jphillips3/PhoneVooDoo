package trio;

import java.io.*;
import java.sql.Timestamp;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author xwei
 * @category This class is used to convert the parsed document node into actual
 *           javascript code
 * 
 */
public class TrioConvert {

	/**
	 * This is the main function which should be called by external classes
	 * 
	 * @param doc
	 * @return boolean value indicating status of conversion
	 */
	boolean convertDocument(Document doc) {
		boolean result = false;
		NodeList nodes = null;
		String code = null;

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					"output.js"));

			if (doc == null || !(doc.hasChildNodes())) {
				return result;
			}

			code = convertRootNode(doc, writer);
			writer.write(code);

			nodes = doc.getChildNodes().item(0).getChildNodes();
			convertChildNodes(nodes, writer);
			writer.close();
			result = true;

		} catch (Exception e) {
			System.err.println("Error writing to file");
			e.printStackTrace();
			return result;
		}
		return result;
	}

	/**
	 * This is used to recursively process all children of a node
	 * 
	 * @param nodes
	 * @param writer
	 */
	void convertChildNodes(NodeList nodes, BufferedWriter writer) {
		String code = null;
		for (int i = 0; i < nodes.getLength(); i++) {
			Node currNode = nodes.item(i);
			if (!currNode.getNodeName().contains("#")) {
				code = convertNodeToCode(currNode, writer);
				writeCode(code, writer);
				if (currNode.hasChildNodes()) {
					convertChildNodes(currNode.getChildNodes(), writer);
				} else {

				}
			}
		}
	}

	/**
	 * This is used to write converted code to output file
	 * 
	 * @param code
	 * @param writer
	 */
	void writeCode(String code, BufferedWriter writer) {
		try {
			writer.write(code);
		} catch (IOException e) {
			System.err.println("Error in writeCode");
			e.printStackTrace();
		}

	}

	/**
	 * This is used to convert the root node: platform specific information
	 * 
	 * @param doc
	 * @param writer
	 * @return converted code
	 */
	String convertRootNode(Document doc, BufferedWriter writer)
			throws IOException {
		String rootCode = null;
		Node root = doc.getChildNodes().item(0);
		writeCode(initFileHeader(), writer);
		rootCode = convertNodeToCode(root, writer);
		return rootCode;
	}

	/**
	 * This is used to check if a node has no child
	 * 
	 * @param input
	 * @return false if it has any child
	 */
	boolean checkEmptyNode(Node input) {
		boolean result = false;
		NodeList nodes = input.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node currNode = nodes.item(i);
			if (!currNode.getNodeName().contains("#")) {
				result = false;
				break;
			} else {
				result = true;
			}
		}
		return result;
	}

	/**
	 * This is used to convert individual nodes with their attributes to
	 * javascript
	 * 
	 * @param input
	 * @param writer
	 * @return converted code
	 */
	String convertNodeToCode(Node input, BufferedWriter writer) {
		String output = null;
		String temp = null;
		String nodeName = input.getNodeName();
		NamedNodeMap attr = input.getAttributes();

		if (nodeName.equalsIgnoreCase("mainview")) {
			if (checkEmptyNode(input)) {
				output = "var mv = getMain();\n";
			} else {
				output = "var mv = getMain();\nmv";
			}
			System.out.println("Generated node: mainview");
		}

		else if (nodeName.equalsIgnoreCase("tabbar")) {
			output = ".tabBar()";
			if (attr.getNamedItem("name") != null) {
				output += "[attr.getNamedItem(\"name\")]";
			}
			System.out.println("Generated node: tabbar");

		}

		else if (nodeName.equalsIgnoreCase("ios")) {
			output = "//This is an iOS automation test.\n"
					+ "UIALogger.logMessage(\"Test starting\");\n"
					+ "var target = UIATarget.localTarget();\n"
					+ "var app = target.frontMostApp();\n"
					+ "var home_window = getMain();\n";
			System.out.println("Generated node: ios");

		}

		else if (nodeName.equalsIgnoreCase("navbar")) {
			if (attr.getNamedItem("index") != null) {
				output = ".navigationBars()["
						+ attr.getNamedItem("index").getNodeValue() + "]";
			} else if (attr.getNamedItem("name") != null) {
				output = ".navigationBars()[\""
						+ attr.getNamedItem("name").getNodeValue() + "\"]";
			} else {
				output = ";//Need an attribute in navigation bars.\n";
				System.err.println("Need an attribute in navigation bars.");
			}
			System.out.println("Generated node: navbar");

		}

		else if (nodeName.equalsIgnoreCase("puts")) {
			try {
				output = "UIALogger.logMessage(\""
						+ attr.getNamedItem("text").getNodeValue() + "\");\n";
			} catch (Exception e) {
				System.err.println("Attribute \"text\" is missing!");
				output = "//Attribute \"text\" is missing in \"puts\"!";
			}
			System.out.println("Generated node: puts");

		}

		else if (nodeName.equalsIgnoreCase("timestamp")) {
			output = "var timestamp = \"" + getTimestamp() + "\"\n";
			System.out.println("Generated node: timestamp");

		}

		else if (nodeName.equalsIgnoreCase("wait")) {
			output = appendWait(output);
			System.out.println("Generated node: wait");

		}

		else if (nodeName.equalsIgnoreCase("textfield")) {
			if (attr.getNamedItem("index") != null) {
				output = ".textFields()["
						+ attr.getNamedItem("index").getNodeValue() + "]";
			} else if (attr.getNamedItem("name") != null) {
				output = ".textFields()[\""
						+ attr.getNamedItem("name").getNodeValue() + "\"]";
			} else {
				output = ";//Need an attribute in textfields.\n";
				System.err.println("Need an attribute in textfields.");
			}

			if (attr.getNamedItem("set") != null) {
				output = output + ".setValue(\""
						+ attr.getNamedItem("set").getNodeValue() + "\");\n";
				output = appendWait(output);

			}
			System.out.println("Generated node: textfield");

		}

		else if (nodeName.equalsIgnoreCase("button")) {
			if (attr.getNamedItem("tap") != null) {
				if (attr.getNamedItem("tap").getNodeValue().equals("false")) {
					output = ";//tap action on button is cancelled.\n";
				} else if (attr.getNamedItem("tap").getNodeValue()
						.equals("true")) {
					if (attr.getNamedItem("index") != null) {
						output = ".buttons()["
								+ attr.getNamedItem("index").getNodeValue()
								+ "].tap();\n";
						output = appendWait(output);
					} else if (attr.getNamedItem("name") != null) {
						output = ".buttons()[\""
								+ attr.getNamedItem("name").getNodeValue()
								+ "\"].tap();\n";
						output = appendWait(output);
					} else {
						output = ";//Need an attribute in buttons.\n";
						System.err.println("Need an attribute in buttons.");
					}
				}
			}
			if (attr.getNamedItem("index") != null) {
				output = ".buttons()["
						+ attr.getNamedItem("index").getNodeValue()
						+ "].tap();\n";
				output = appendWait(output);
			} else if (attr.getNamedItem("name") != null) {
				output = ".buttons()[\""
						+ attr.getNamedItem("name").getNodeValue()
						+ "\"].tap();\n";
				output = appendWait(output);
			} else {
				output = ";//Need an attribute in buttons.\n";
				System.err.println("Need an attribute in buttons.");
			}
			System.out.println("Generated node: button");

		}

		else if (nodeName.equalsIgnoreCase("cell")) {

			if (attr.getNamedItem("index") != null) {
				output = ".cells()["
						+ attr.getNamedItem("index").getNodeValue() + "]";
			} else if (attr.getNamedItem("name") != null) {
				output = ".cells()[\""
						+ attr.getNamedItem("name").getNodeValue() + "\"]";
			} else {
				output = ";//Need an attribute in cells.\n";
				System.err.println("Need an attribute in cells.");
			}
			if (attr.getNamedItem("tap") != null) {
				if (attr.getNamedItem("tap").getNodeValue().equals("false")) {
					output = ";//tap action on table cell is cancelled.\n";
				} else if (attr.getNamedItem("tap").getNodeValue()
						.equals("true")) {
					if (attr.getNamedItem("index") != null) {
						output = ".cells()["
								+ attr.getNamedItem("index").getNodeValue()
								+ "].tap();\n";
						output = appendWait(output);
					} else if (attr.getNamedItem("name") != null) {
						output = ".cells()[\""
								+ attr.getNamedItem("name").getNodeValue()
								+ "\"].tap();\n";
						output = appendWait(output);
					} else {
						output = ";//Need an attribute in cells.\n";
						System.err.println("Need an attribute in cells.");
					}
				}
			}

			if (attr.getNamedItem("assert") != null) {
				if (attr.getNamedItem("assert").getNodeValue().equals("true")) {
					output = output + ".scrollToElementWithName(\""
							+ attr.getNamedItem("name").getNodeValue() + "\")";
					temp = "mv.tableViews()[0]" + output;
					output = output
							+ ";\nif(("
							+ temp
							+ ").isVisible()){UIALogger.logPass(\"Assertion Passed\");}else{UIALogger.logFail(\"Assertion Failed\");}\n";

				} else if (attr.getNamedItem("assert").getNodeValue()
						.equals("false")) {
					output = output + ".scrollToElementWithName(\""
							+ attr.getNamedItem("name").getNodeValue()
							+ "\");\n";
					temp = output;
				} else {
				}
			}
			System.out.println("Generated node: cell");

		}

		else if (nodeName.equalsIgnoreCase("table")) {
			if (attr.getNamedItem("index") != null) {
				output = ".tableViews()["
						+ attr.getNamedItem("index").getNodeValue() + "]";
			} else if (attr.getNamedItem("name") != null) {
				output = ".tableViews()[\""
						+ attr.getNamedItem("name").getNodeValue() + "\"]";
			} else {
				output = ";//Need an attribute in table.\n";
				System.err.println("Need an attribute in table.");
			}
			System.out.println("Generated node: table");

		}

		else if (nodeName.equalsIgnoreCase("dump")) {
			output = "dump(mv);\n";
			System.out.println("Generated node: dump");

		}

		else if (nodeName.equalsIgnoreCase("scroll")) {
			if (attr.getNamedItem("going") != null) {
				if (attr.getNamedItem("going").getNodeValue().equals("down")) {
					output = ".scrollDown();\n";
					output = appendWait(output);
				} else if (attr.getNamedItem("going").getNodeValue()
						.equals("up")) {
					output = ".scrollUp();\n";
					output = appendWait(output);

				}
			}
			System.out.println("Generated node: scroll");

		}

		else if (nodeName.equalsIgnoreCase("scrollTo")) {
			if (attr.getNamedItem("name") != null) {
				output = ".scrollToElementWithName(\""
						+ attr.getNamedItem("name").getNodeValue() + "\");\n";
				output = appendWait(output);
			}
			System.out.println("Generated node: scrollTo");

		}

		else if (nodeName.equalsIgnoreCase("taptap")) {
			if (attr.getNamedItem("x") != null
					&& attr.getNamedItem("y") != null) {
				output = ".tapWithOptions({x:"
						+ attr.getNamedItem("x").getNodeValue() + ", y:"
						+ attr.getNamedItem("y").getNodeValue() + "});\n";
			} else {
				output = ";//Need x and/or y coordinates to tap.\n";
				System.err.println("Need x and/or y coordinates to tap.");
			}
			System.out.println("Generated node: taptap");

		}

		else {
			System.err.println("Invalid node elements.");
		}
		return output;
	}

	/**
	 * This is used to generate timestamps as var
	 * 
	 * @return timestamp
	 */
	String getTimestamp() {
		java.util.Date date = new java.util.Date();
		return (new Timestamp(date.getTime())).toString();
	}

	/**
	 * This method automatically appends manual wait
	 * 
	 * @param output
	 * @return
	 */
	String appendWait(String output) {
		return output = output + "wait(1);\n";
	}

	/**
	 * This is used to generated necessary sub-procedures for platform specific
	 * tests
	 * 
	 * @return generated code in string
	 */
	String initFileHeader() {
		String initHeader = null;
		initHeader = "function getMain(){"
				+ "var rtnWin = app.mainWindow();"
				+ "return rtnWin;}"
				+ "\n"
				+ "function dump(curr){"
				+ "curr.logElementTree();"
				+ "UIALogger.logMessage(curr);}"
				+ "\n"
				+ "function wait(timer){"
				+ "if (timer >= 0){"
				+ "UIALogger.logMessage(\"Waiting for \" + timer + \" second(s).\");"
				+ "target.delay(timer);}" + "else{"
				+ "UIALogger.logWarning(\"negative wait\");"
				+ "target.delay(1);}}" + "\n"
				+ "function elementVisible(element, wait, freq){"
				+ "if (freq == null){" + "freq = 1;}" + "if (wait == null){"
				+ "wait = 5;}" + "var step = wait/freq;"
				+ "for (var i=0; i<step; i++){" + "if (element.isVisible()){"
				+ "return true;}" + "wait(freq);}" + "element.logElement();"
				+ "throw(\"Element Not Visible\");" + "return false;}" + "\n";
		return initHeader;
	}

}
