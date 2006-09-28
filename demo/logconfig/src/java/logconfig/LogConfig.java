package logconfig;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.wings.ReloadManager;
import org.wings.SBorderLayout;
import org.wings.SBoxLayout;
import org.wings.SButton;
import org.wings.SButtonGroup;
import org.wings.SCheckBox;
import org.wings.SComboBox;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SContainer;
import org.wings.SDimension;
import org.wings.SForm;
import org.wings.SFrame;
import org.wings.SGridLayout;
import org.wings.SLabel;
import org.wings.SList;
import org.wings.SPanel;
import org.wings.SRadioButton;
import org.wings.SResourceIcon;
import org.wings.SScrollPane;
import org.wings.SScrollPaneLayout;
import org.wings.SSpacer;
import org.wings.STextField;
import org.wings.STree;
import org.wings.SURLIcon;
import org.wings.border.SEmptyBorder;
import org.wings.border.SLineBorder;
import org.wings.header.Link;
import org.wings.resource.DefaultURLResource;
import org.wings.script.JavaScriptEvent;
import org.wings.script.JavaScriptListener;
import org.wings.session.SessionManager;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public class LogConfig {
    private static final Log log = LogFactory.getLog(LogConfig.class);
    private static final SDimension BU_DIM = new SDimension(100, SDimension.AUTO_INT);
    private static final SDimension IN_DIM = new SDimension(200, SDimension.AUTO_INT);

    private SFrame mainFrame = new SFrame(":: Log4j - Configuration ::");

    private Document document;
    private DomModel treeModel;
    private Vector listModel;
    private Element rootNode;
    private Node selectedNode;

    private SForm fo_form;
    private STree tr_domTree;
    private SRadioButton rb_insertNode;
    private SRadioButton rb_updateNode;
    private STextField tf_editCategoryName;
    private STextField tf_editPriorityValue;
    private SComboBox cb_editAdditivityFlag;
    private SList li_editAppenderRef;
    private SLabel la_status;
    private SButton bu_saveNode;
    private SButton bu_deleteNode;
    private SButton bu_commitChanges;

    public LogConfig() {
    	final String configFile = SessionManager.getSession().getProperty("log4j.xml.path").toString();
        try {
            SAXReader reader = new SAXReader(false);
            reader.setEntityResolver(new EntityResolver() {
                public InputSource resolveEntity(String publicId, String systemId) {
                    if (systemId != null && systemId.endsWith("log4j.dtd")) {
                        InputStream in = getClass().getResourceAsStream("../resources/log4j.dtd");
                        return new InputSource(in);
                    }
                    return null;
                }
            });
            // reader.setIgnoreComments(true);
            document = reader.read(new File(configFile));
        } catch (Exception ex) {
            log.error("Could not load config file!", ex);
        }

        rootNode = document.getRootElement();
        treeModel = new DomModel(rootNode);

        tr_domTree = new STree(treeModel);
        tr_domTree.setCellRenderer(new DomRenderer());
        tr_domTree.setPreferredSize(new SDimension(450, 510));
        tr_domTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tr_domTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent tse) {
                selectedNode = (Node) tse.getPath().getLastPathComponent();
                Node categoryNode = selectedNode.selectSingleNode("ancestor-or-self::category");

                if (tr_domTree.getSelectionCount() > 0 && categoryNode != null) {
                    fillEditFields(categoryNode);
                    rb_updateNode.setSelected(true);
                    rb_updateNode.setEnabled(true);
                    bu_deleteNode.setEnabled(true);
                } else {
                    clearEditFields();
                    rb_insertNode.setSelected(true);
                    rb_updateNode.setEnabled(false);
                    bu_deleteNode.setEnabled(false);
                }
            }
        });

        SScrollPane sp_tree = new SScrollPane(tr_domTree);
        // sp_tree.setVerticalExtent(30);
        sp_tree.setHorizontalExtent(1);
        sp_tree.setBorder(new SLineBorder(1));
        sp_tree.getVerticalScrollBar().setBlockIncrement(3);
        sp_tree.getHorizontalScrollBar().setBlockIncrement(3);
        sp_tree.setPreferredSize(new SDimension(470, 528));
        // (SScrollBar) sp_tree.getVerticalScrollBar()).setShowAsFormComponent(false);
        // ((SScrollBar) sp_tree.getHorizontalScrollBar()).setShowAsFormComponent(false);
        ((SScrollPaneLayout) sp_tree.getLayout()).setPaging(false);

        SButtonGroup bg_insertOrUpdate = new SButtonGroup();
        rb_insertNode = new SRadioButton("Insert node");
        rb_insertNode.setSelected(true);
        rb_insertNode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (rb_insertNode.isSelected()) {
                    clearEditFields();
                }
            }
        });
        rb_updateNode = new SRadioButton("Update node");
        rb_updateNode.setEnabled(false);
        rb_updateNode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (rb_updateNode.isSelected()) {
                    fillEditFields(selectedNode.selectSingleNode("ancestor-or-self::category"));
                }
            }
        });
        bg_insertOrUpdate.add(rb_insertNode);
        bg_insertOrUpdate.add(rb_updateNode);

        tf_editCategoryName = new STextField();
        tf_editCategoryName.setPreferredSize(IN_DIM);
        tf_editCategoryName.setHorizontalAlignment(SConstants.LEFT);
        tf_editPriorityValue = new STextField();
        tf_editPriorityValue.setPreferredSize(IN_DIM);
        tf_editPriorityValue.setHorizontalAlignment(SConstants.LEFT);

        String[] additivityModel = {"true", "false"};
        cb_editAdditivityFlag = new SComboBox(additivityModel);
        cb_editAdditivityFlag.setPreferredSize(IN_DIM);
        cb_editAdditivityFlag.setHorizontalAlignment(SConstants.LEFT);

        listModel = new Vector();
        List appenderNames = rootNode.selectNodes("./appender/@name");
        for (Iterator i = appenderNames.iterator(); i.hasNext();) {
            listModel.add(((Node) i.next()).getText());
        }
        li_editAppenderRef = new SList(listModel);
        li_editAppenderRef.setVisibleRowCount(2);
        li_editAppenderRef.setPreferredSize(IN_DIM);
        li_editAppenderRef.setHorizontalAlignment(SConstants.LEFT);

        bu_saveNode = new SButton("Insert", new SURLIcon("../icons/insert.gif"));
        bu_saveNode.setPreferredSize(BU_DIM);
        bu_saveNode.setHorizontalAlignment(SConstants.LEFT);
        bu_saveNode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveEditFields();
            }
        });

        bu_deleteNode = new SButton("Delete", new SURLIcon("../icons/delete.gif"));
        bu_deleteNode.setDisabledIcon(new SURLIcon("../icons/deleteDisabled.gif"));
        bu_deleteNode.setPreferredSize(BU_DIM);
        bu_deleteNode.setHorizontalAlignment(SConstants.LEFT);
        bu_deleteNode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Element categoryNode = getSelectedCategory();
                if (categoryNode == null) {
                    throw new IllegalStateException("a category must be selected for deletion");
                }

                Node[] path = { rootNode };
                int[] childIndices = { treeModel.getIndexOfChild(rootNode, categoryNode) };
                Node[] children = { categoryNode };
                categoryNode.detach();
                treeModel.fireTreeNodesRemoved(
                        new TreeModelEvent(LogConfig.this, path, childIndices, children));

                clearEditFields();
                rb_insertNode.setSelected(true);
                rb_updateNode.setEnabled(false);
                bu_deleteNode.setEnabled(false);
            }
        });
        bu_deleteNode.setEnabled(false);

        bu_commitChanges = new SButton("Commit", new SURLIcon("../icons/commit.gif"));
        bu_commitChanges.setPreferredSize(BU_DIM);
        bu_commitChanges.setHorizontalAlignment(SConstants.LEFT);
        bu_commitChanges.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    XMLWriter writer = new XMLWriter(new FileWriter(configFile), OutputFormat.createPrettyPrint());
                    writer.write(document);
                    writer.close();
                    la_status.setText("Your changes have been successfully written to 'log4j.xml'!");
                } catch (IOException ex) {
                    log.error("Could not write file!", ex);
                    la_status.setText("Couldn't write changes to 'log4j.xml'! See log for details.");
                }
            }
        });

        SPanel pa_edit = new SPanel();
        styleControlPanel(pa_edit, "Insert / update category nodes:");
        pa_edit.add(new SSpacer(0, 5));
        SPanel pa_mode = new SPanel(new SGridLayout(2));
        pa_mode.setPreferredSize(SDimension.FULLWIDTH);
        rb_insertNode.setHorizontalAlignment(SConstants.LEFT);
        rb_updateNode.setHorizontalAlignment(SConstants.RIGHT);
        pa_mode.add(rb_insertNode);
        pa_mode.add(rb_updateNode);
        pa_edit.add(pa_mode);
        pa_edit.add(new SSpacer(0, 5));
        pa_edit.add(new SLabel("Category name:"));
        pa_edit.add(tf_editCategoryName);
        pa_edit.add(new SLabel("Priority value:"));
        pa_edit.add(tf_editPriorityValue);
        pa_edit.add(new SLabel("Additivity flag:"));
        pa_edit.add(cb_editAdditivityFlag);
        pa_edit.add(new SLabel("Appender reference:"));
        pa_edit.add(li_editAppenderRef);
        pa_edit.add(bu_saveNode);

        SPanel pa_delete = new SPanel();
        styleControlPanel(pa_delete, "Delete selected category node:");
        pa_delete.add(new SSpacer(0, 5));
        pa_delete.add(bu_deleteNode);

        SPanel pa_commit = new SPanel();
        styleControlPanel(pa_commit, "Commit changes to 'log4j.xml':");
        pa_commit.add(new SSpacer(0, 5));
        pa_commit.add(bu_commitChanges);

        SPanel pa_controls = new SPanel(new SBoxLayout(SBoxLayout.VERTICAL));
        pa_controls.add(new SSpacer(0, 25));
        pa_controls.add(pa_edit);
        pa_controls.add(new SSpacer(0, 25));
        pa_controls.add(pa_delete);
        pa_controls.add(new SSpacer(0, 25));
        pa_controls.add(pa_commit);
        pa_controls.add(new SSpacer(0, 25));

        fo_form = new SForm(new SGridLayout(1, 3, 25, 0));
        fo_form.setBorder(new SLineBorder(1));
        fo_form.add(sp_tree);
        fo_form.add(pa_controls);
        fo_form.add(createDebugPanel());

        SPanel pa_header = new SPanel(new SGridLayout());
        pa_header.setBackground(new Color(60, 80, 100));
        pa_header.setBorder(new SEmptyBorder(10, 15, 10, 0));
        pa_header.setPreferredSize(SDimension.FULLWIDTH);
        pa_header.setHorizontalAlignment(SConstants.LEFT);
        SLabel la_header = new SLabel("Log4j - Configuration");
        la_header.setStyle(la_header.getStyle() + " la_header");
        la_header.setVerticalAlignment(SConstants.CENTER);
        la_header.setHorizontalAlignment(SConstants.LEFT);
        pa_header.add(la_header);

        SPanel pa_footer = new SPanel();
        pa_footer.setBorder(new SEmptyBorder(5, 15, 0, 0));
        la_status = new SLabel();
        la_status.setStyle(la_status.getStyle() + " la_status");
        pa_footer.add(la_status);

        SContainer cp = mainFrame.getContentPane();
        cp.add(pa_header, SBorderLayout.NORTH);
        cp.add(fo_form, SBorderLayout.CENTER);
        cp.add(pa_footer, SBorderLayout.SOUTH);
        mainFrame.addHeader(new Link("stylesheet", null, "text/css", null,
                new DefaultURLResource("../css/custom.css")));
        mainFrame.setVisible(true);

        // TESTING HISTORY
        if (false) {
            mainFrame.setNoCaching(false);
            tr_domTree.setEpochCheckEnabled(false);
            // fo_form.setPostMethod(false);
        }

//		try {
//			java.net.URL templateURL = SessionManager.getSession()
//				.getServletContext().getResource("/templates/main.thtml");
//			if (templateURL == null) {
//				mainFrame.getContentPane().add(new SLabel("Could not find" +
//						"template file! Are you using a JAR-File?"));
//				return;
//			}
//			mainFrame.getContentPane().setLayout(new STemplateLayout(templateURL));
//		} catch (java.io.IOException ex) {
//			log.error("Could not find template file!", ex);
//		}
    }

//	private Vector getExpandedPaths() {
//		Vector expandedPaths = new Vector();
//		for (int i = 0; i < tr_domTree.getRowCount(); ++i) {
//			TreePath path = tr_domTree.getPathForRow(i);
//			if (tr_domTree.isExpanded(path)) {
//				expandedPaths.add(path);
//			}
//		}
//		return expandedPaths;
//	}
//
//	private void setExpandedPaths(Vector expandedPaths) {
//		for (int i = 0; i < expandedPaths.size(); ++i) {
//			tr_domTree.expandRow((TreePath) expandedPaths.get(i));
//		}
//	}

    private void saveEditFields() {
        String catName = tf_editCategoryName.getText();
        String priValue = tf_editPriorityValue.getText();
        String addFlag = cb_editAdditivityFlag.getSelectedItem().toString();
        Object[] appRefNames = li_editAppenderRef.getSelectedValues();

        if (catName.equals("") || priValue.equals("")) {
            la_status.setText("You have to provide at least a category name and a priority value!");
            return;
        }

        if (rb_insertNode.isSelected()) {
            Element categoryNode = DocumentHelper.createElement("category").addAttribute("name", catName);
            categoryNode.addElement("priority").addAttribute("value", priValue);
            categoryNode.addAttribute("additivity", addFlag);
            for (int i = 0; i < appRefNames.length; ++i) {
                categoryNode.addElement("appender-ref").addAttribute("ref", appRefNames[i].toString());
            }

            Node firstCategory = rootNode.selectSingleNode("./category[1]");
            if (firstCategory == null) {
                rootNode.add(categoryNode);
            } else {
                rootNode.content().add(rootNode.indexOf(firstCategory), categoryNode);
            }
            int index = rootNode.selectNodes("./* | ./@*").indexOf(categoryNode);

            Node[] path = { rootNode };
            int[] childIndices = { index };
            Node[] children = { categoryNode };
            treeModel.fireTreeNodesInserted(new TreeModelEvent(LogConfig.this, path, childIndices, children));

            selectedNode = categoryNode;
            Node[] selectedPath = { rootNode, categoryNode};
            tr_domTree.setSelectionPath(new TreePath(selectedPath));

            fillEditFields(categoryNode);
        } else if (rb_updateNode.isSelected()) {
            Element categoryNode = getSelectedCategory();
            if (categoryNode == null) {
                throw new IllegalStateException("a category must be selected for update");
            }
            categoryNode.selectSingleNode("./@name").setText(catName);
            categoryNode.selectSingleNode("./priority/@value").setText(priValue);
            categoryNode.selectSingleNode("./@additivity").setText(addFlag);

            Node[] path = { rootNode, categoryNode };

            List oldAppRefNodes = categoryNode.selectNodes("./appender-ref");
            int[] oldChildIndices = new int[oldAppRefNodes.size()];
            for (int i = 0; i < oldAppRefNodes.size(); ++i) {
                Node oldAppenderNode = (Node) oldAppRefNodes.get(i);
                oldChildIndices[i] = treeModel.getIndexOfChild(categoryNode, oldAppenderNode);
                oldAppenderNode.detach();
            }
            treeModel.fireTreeNodesRemoved(
                    new TreeModelEvent(LogConfig.this, path, oldChildIndices, oldAppRefNodes.toArray()));

            List newAppRefNodes = new Vector();
            int[] newChildIndices = new int[appRefNames.length];
            for (int i = 0; i < appRefNames.length; ++i) {
                Node newAppenderNode = (Node) categoryNode.addElement("appender-ref").addAttribute("ref",
                        appRefNames[i].toString());
                newAppRefNodes.add(newAppenderNode);
                newChildIndices[i] = treeModel.getIndexOfChild(categoryNode, newAppenderNode);
            }
            treeModel.fireTreeNodesInserted(
                    new TreeModelEvent(LogConfig.this, path, newChildIndices, newAppRefNodes.toArray()));

            selectedNode = categoryNode;
            tr_domTree.setSelectionPath(new TreePath(path));
        }
    }

    private void clearEditFields() {
        tf_editCategoryName.setText("");
        tf_editPriorityValue.setText("");
        cb_editAdditivityFlag.setSelectedItem("true");
        li_editAppenderRef.clearSelection();
        la_status.setText("");
        bu_saveNode.setText("Insert");
        bu_saveNode.setIcon(new SURLIcon("../icons/insert.gif"));
    }

    private void fillEditFields(Node categoryNode) {
        tf_editCategoryName.setText(categoryNode.valueOf("./@name"));
        tf_editPriorityValue.setText(categoryNode.valueOf("./priority/@value"));
        cb_editAdditivityFlag.setSelectedItem(categoryNode.valueOf("./@additivity"));
        List appenderRefs = categoryNode.selectNodes("./appender-ref/@ref");
        int[] selectedIndices = new int[appenderRefs.size()];
        for (int i = 0; i < appenderRefs.size(); ++i) {
            selectedIndices[i] = listModel.indexOf(((Node) appenderRefs.get(i)).getText());
        }
        li_editAppenderRef.setSelectedIndices(selectedIndices);
        la_status.setText("");
        bu_saveNode.setText("Update");
        bu_saveNode.setIcon(new SURLIcon("../icons/update.gif"));
    }

    private Element getSelectedCategory() {
        if (selectedNode == null) return null;
        Node selectedCategoryNode = selectedNode.selectSingleNode("ancestor-or-self::category");
        if (selectedCategoryNode == null) return null;

        List categoryElements = rootNode.elements("category");
        for (Iterator i = categoryElements.iterator(); i.hasNext();) {
            Element currentElement = (Element) i.next();
            if (currentElement == selectedCategoryNode) {
                return currentElement;
            }
        }
        return null;
    }

    private void styleControlPanel(SPanel panel, String title) {
        SBoxLayout boxLayout = new SBoxLayout(SBoxLayout.VERTICAL);
        boxLayout.setVgap(5);
        panel.setLayout(boxLayout);
        panel.setVerticalAlignment(SConstants.TOP);
        panel.setBackground(new Color(210, 230, 250));
        panel.setBorder(new SLineBorder(1, new Insets(10, 10, 10, 10)));
        panel.setPreferredSize(new SDimension(220, SDimension.AUTO_INT));
        SLabel la_title = new SLabel(title);
        la_title.setStyle(la_title.getStyle() + " la_title");
        panel.add(la_title);
    }

    private SPanel createDebugPanel() {
        SBoxLayout boxLayout = new SBoxLayout(SBoxLayout.VERTICAL);
        boxLayout.setVgap(5);

        SPanel pa_debug = new SPanel(boxLayout);
        pa_debug.setVerticalAlignment(SConstants.TOP);
        pa_debug.setBorder(new SLineBorder(1, new Insets(10, 10, 10, 10)));
        pa_debug.setPreferredSize(new SDimension(SDimension.AUTO_INT, 528));
        SLabel la_title = new SLabel("Playground for debugging some AJAX stuff:");
        la_title.setStyle(la_title.getStyle() + " la_title");
        pa_debug.add(la_title);

        boolean selected;
        final String[] cb_texts = {
                "Frame: incrementalUpdateEnabled = ",
                "Frame: incrementalUpdateHighlight = ",
                "Frame: incrementalUpdateCursor = ",
                "Form: completeUpdateForced = ",
                "Tree: completeUpdateForced = ",
                "Link: completeUpdateForced = "
        };

        selected = mainFrame.isIncrementalUpdateEnabled();
        final SCheckBox cb_toggleFrameIncrementalUpdate =
            new SCheckBox(cb_texts[0] + selected, selected);
        cb_toggleFrameIncrementalUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean state = mainFrame.isIncrementalUpdateEnabled();
                mainFrame.setIncrementalUpdateEnabled(!state);
                cb_toggleFrameIncrementalUpdate.setText(cb_texts[0] + !state);
            }
        });

        selected = ((Boolean) mainFrame.getIncrementalUpdateHighlight()[0]).booleanValue();
        final SCheckBox cb_toggleFrameUpdateHighlight =
            new SCheckBox(cb_texts[1] + selected, selected);
        cb_toggleFrameUpdateHighlight.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean state = ((Boolean) mainFrame.getIncrementalUpdateHighlight()[0]).booleanValue();
                mainFrame.setIncrementalUpdateHighlight(!state, "#FFFF99", 300);
                cb_toggleFrameUpdateHighlight.setText(cb_texts[1] + !state);
            }
        });

        selected = mainFrame.isIncrementalUpdateCursor();
        final SCheckBox cb_toggleFrameUpdateCursor =
            new SCheckBox(cb_texts[2] + selected, selected);
        cb_toggleFrameUpdateCursor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean state = mainFrame.isIncrementalUpdateCursor();
                mainFrame.setIncrementalUpdateCursor(!state);
                cb_toggleFrameUpdateCursor.setText(cb_texts[2] + !state);
            }
        });

        selected = fo_form.isCompleteUpdateForced();
        final SCheckBox cb_toggleFormCompleteUpdate =
            new SCheckBox(cb_texts[3] + selected, selected);
        cb_toggleFormCompleteUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean state = fo_form.isCompleteUpdateForced();
                fo_form.setCompleteUpdateForced(!state);
                cb_toggleFormCompleteUpdate.setText(cb_texts[3]  + !state);
            }
        });

        selected = tr_domTree.isCompleteUpdateForced();
        final SCheckBox cb_toggleTreeCompleteUpdate =
            new SCheckBox(cb_texts[4] + selected, selected);
        cb_toggleTreeCompleteUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean state = tr_domTree.isCompleteUpdateForced();
                tr_domTree.setCompleteUpdateForced(!state);
                cb_toggleTreeCompleteUpdate.setText(cb_texts[4]  + !state);
            }
        });

        selected = tr_domTree.isCompleteUpdateForced();
        final SCheckBox cb_toggleCheckboxTest =
            new SCheckBox(cb_texts[5] + selected, selected);
        cb_toggleCheckboxTest.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean state = cb_toggleTreeCompleteUpdate.isCompleteUpdateForced();
                cb_toggleTreeCompleteUpdate.setCompleteUpdateForced(!state);
                cb_toggleCheckboxTest.setText(cb_texts[5]  + !state);
            }
        });

        final SButton bu_markFrameDirty = new SButton("Reload the entire frame / mark it dirty!");
        bu_markFrameDirty.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.reload(ReloadManager.STATE);
            }
        });

        final SButton bu_doSomethingSpecial = new SButton("Do something special for 10 seconds!");
        bu_doSomethingSpecial.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e1) {}
            }
        });

        final SButton bu_forceServerError = new SButton("Force an \"Internal Server Error\"! (500)");
        bu_forceServerError.setName("force_error");

        addToDebugPanel(pa_debug, new SSpacer(0, 10));
        addToDebugPanel(pa_debug, cb_toggleFrameIncrementalUpdate);
        addToDebugPanel(pa_debug, cb_toggleFrameUpdateHighlight);
        addToDebugPanel(pa_debug, cb_toggleFrameUpdateCursor);
        addToDebugPanel(pa_debug, cb_toggleFormCompleteUpdate);
        addToDebugPanel(pa_debug, cb_toggleTreeCompleteUpdate);
        addToDebugPanel(pa_debug, cb_toggleCheckboxTest);

        addToDebugPanel(pa_debug, new SSpacer(0, 10));
        addToDebugPanel(pa_debug, bu_markFrameDirty);
        addToDebugPanel(pa_debug, bu_doSomethingSpecial);
        addToDebugPanel(pa_debug, bu_forceServerError);

        addToDebugPanel(pa_debug, new SSpacer(0, 10));
        addToDebugPanel(pa_debug, createRandomLinkPanel());

        SLabel la_activityIndicator = new SLabel("<html><b>&nbsp;Loading...</b></html>",
                new SResourceIcon("org/wings/icons/AjaxActivityIndicator.gif"));
        la_activityIndicator.setName("ajaxActivityIndicator");
        la_activityIndicator.setHorizontalAlignment(SConstants.CENTER);
        pa_debug.add(new SSpacer(0, 25));
        pa_debug.add(la_activityIndicator);

        SPanel pa_wrapper = new SPanel(new SBoxLayout(SBoxLayout.VERTICAL));
        pa_wrapper.add(new SSpacer(0, 25));
        pa_wrapper.add(pa_debug);
        pa_wrapper.setVerticalAlignment(SConstants.TOP);

        return pa_wrapper;
    }

    private void addToDebugPanel(SPanel debug, SComponent button) {
        button.setHorizontalAlignment(SConstants.LEFT);
        debug.add(button);
    }

    private SPanel createRandomLinkPanel() {
        int rows = 6;

        class RandomLinkGenerator implements ActionListener {
            private SLabel label;
            private boolean sleep;

            public RandomLinkGenerator(SLabel label, boolean sleep) {
                this.label = label;
                this.sleep = sleep;
            }

            public void actionPerformed(ActionEvent e) {
                if (sleep) {
                    int ms = new Random().nextInt(3001);
                    try {
                        Thread.sleep(ms);
                    } catch (InterruptedException e1) {}
                    label.setText("Woke up after " + ms + " ms.");
                } else {
                    int nr = new Random().nextInt(9000) + 1000;
                    label.setText("Ok, my number is " + nr + ".");
                }
            }
        }

        SPanel panel = new SPanel(new SGridLayout(rows, 2, 5, 5));
        for (int i = 0; i < rows; ++i) {
            SLabel label = new SLabel();
            SButton button = new SButton();
            button.setShowAsFormComponent(false);
            button.setHorizontalAlignment(SConstants.LEFT);
            if (i < rows - 1) {
                button.setText("Sleep for a while!  >>");
                button.addActionListener(new RandomLinkGenerator(label, true));
            } else {
                button.setText("Say any number!  >>");
                button.addActionListener(new RandomLinkGenerator(label, false));
                panel.add(new SSpacer(0, 10));
                panel.add(new SSpacer(0, 10));
            }
            panel.add(button);
            panel.add(label);
        }
        return panel;
    }

}