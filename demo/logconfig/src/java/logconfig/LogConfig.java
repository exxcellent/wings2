package logconfig;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
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
import org.wings.SAnchor;
import org.wings.SBoxLayout;
import org.wings.SButton;
import org.wings.SButtonGroup;
import org.wings.SCheckBox;
import org.wings.SComboBox;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SContainer;
import org.wings.SDefaultListModel;
import org.wings.SDimension;
import org.wings.SForm;
import org.wings.SFrame;
import org.wings.SGridLayout;
import org.wings.SLabel;
import org.wings.SList;
import org.wings.SMenu;
import org.wings.SMenuBar;
import org.wings.SMenuItem;
import org.wings.SPageScroller;
import org.wings.SPanel;
import org.wings.SRadioButton;
import org.wings.SScrollBar;
import org.wings.SScrollPane;
import org.wings.SSpacer;
import org.wings.STable;
import org.wings.STemplateLayout;
import org.wings.STextArea;
import org.wings.STextField;
import org.wings.STree;
import org.wings.SURLIcon;
import org.wings.event.SDocumentEvent;
import org.wings.event.SDocumentListener;
import org.wings.header.JavaScriptHeader;
import org.wings.header.Link;
import org.wings.header.Script;
import org.wings.header.StyleSheetHeader;
import org.wings.script.JavaScriptEvent;
import org.wings.script.JavaScriptListener;
import org.wings.session.SessionManager;
import org.wings.style.CSSProperty;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public class LogConfig {
    private static final Log log = LogFactory.getLog(LogConfig.class);
    private static final SURLIcon INSERT_IMG = new SURLIcon("../images/insert.gif");
    private static final SURLIcon UPDATE_IMG = new SURLIcon("../images/update.gif");
    private static final SDimension BU_DIM = new SDimension(100, SDimension.AUTO_INT);
    private static final SDimension IN_DIM = SDimension.FULLWIDTH;

    private SFrame fr_frame = new SFrame("Log4J - Configurator");

    private Document document;
    private DomModel treeModel;
    private Vector listModel;
    private Element rootNode;
    private Node selectedNode;

    private SForm fo_form;
    private STree tr_domTree;
    private SScrollPane sp_tree;
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
        tr_domTree.setPreferredSize(SDimension.FULLWIDTH);
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

        sp_tree = new SScrollPane(tr_domTree);
        sp_tree.setStyle(sp_tree.getStyle() + " sp_tree");
        sp_tree.setPreferredSize(new SDimension(477, SDimension.AUTO_INT));
        sp_tree.setVerticalAlignment(SConstants.TOP_ALIGN);
        sp_tree.getVerticalScrollBar().setBlockIncrement(3);
        sp_tree.setVerticalExtent(25);

        SButtonGroup bg_insertOrUpdate = new SButtonGroup();
        rb_insertNode = new SRadioButton("Insert node");
        rb_insertNode.setSelected(true);
        rb_insertNode.setShowAsFormComponent(false);
        rb_insertNode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (rb_insertNode.isSelected()) {
                  clearEditFields();
              }
            }
        });
        rb_updateNode = new SRadioButton("Update node");
        rb_updateNode.setEnabled(false);
        rb_updateNode.setShowAsFormComponent(false);
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
        tf_editCategoryName.setHorizontalAlignment(SConstants.LEFT_ALIGN);
        tf_editPriorityValue = new STextField();
        tf_editPriorityValue.setPreferredSize(IN_DIM);
        tf_editPriorityValue.setHorizontalAlignment(SConstants.LEFT_ALIGN);

        String[] additivityModel = {"true", "false"};
        cb_editAdditivityFlag = new SComboBox(additivityModel);
        cb_editAdditivityFlag.setPreferredSize(IN_DIM);
        cb_editAdditivityFlag.setHorizontalAlignment(SConstants.LEFT_ALIGN);

        listModel = new Vector();
        List appenderNames = rootNode.selectNodes("./appender/@name");
        for (Iterator i = appenderNames.iterator(); i.hasNext();) {
            listModel.add(((Node) i.next()).getText());
        }
        li_editAppenderRef = new SList(listModel);
        li_editAppenderRef.setVisibleRowCount(2);
        li_editAppenderRef.setPreferredSize(IN_DIM);
        li_editAppenderRef.setHorizontalAlignment(SConstants.LEFT_ALIGN);

        bu_saveNode = new SButton("Insert", new SURLIcon("../images/insert.gif"));
        bu_saveNode.setPreferredSize(BU_DIM);
        bu_saveNode.setHorizontalAlignment(SConstants.LEFT_ALIGN);
        bu_saveNode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveEditFields();
            }
        });

        bu_deleteNode = new SButton("Delete", new SURLIcon("../images/delete.gif"));
        bu_deleteNode.setDisabledIcon(new SURLIcon("../images/delete_disabled.gif"));
        bu_deleteNode.setPreferredSize(BU_DIM);
        bu_deleteNode.setHorizontalAlignment(SConstants.LEFT_ALIGN);
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

        bu_commitChanges = new SButton("Commit", new SURLIcon("../images/commit.gif"));
        bu_commitChanges.setPreferredSize(BU_DIM);
        bu_commitChanges.setHorizontalAlignment(SConstants.LEFT_ALIGN);
        bu_commitChanges.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    XMLWriter writer = new XMLWriter(new FileWriter(configFile), OutputFormat.createPrettyPrint());
                    writer.write(document);
                    writer.close();
                    la_status.setText("  Your changes have been successfully written to 'log4j.xml'!");
                } catch (IOException ex) {
                    log.error("Could not write file!", ex);
                    la_status.setText("  Couldn't write changes to 'log4j.xml'! See log for details.");
                }
            }
        });

        SPanel pa_edit = createControlPanel("Insert / update category nodes:");
        SPanel pa_mode = new SPanel(new SGridLayout(2));
        pa_mode.setStyle(pa_mode.getStyle() + " pa_mode");
        pa_mode.setPreferredSize(SDimension.FULLWIDTH);
        rb_insertNode.setHorizontalAlignment(SConstants.LEFT_ALIGN);
        rb_updateNode.setHorizontalAlignment(SConstants.RIGHT_ALIGN);
        pa_mode.add(rb_insertNode);
        pa_mode.add(rb_updateNode);
        pa_edit.add(pa_mode);
        pa_edit.add(new SLabel("Category name:"));
        pa_edit.add(tf_editCategoryName);
        pa_edit.add(new SLabel("Priority value:"));
        pa_edit.add(tf_editPriorityValue);
        pa_edit.add(new SLabel("Additivity flag:"));
        pa_edit.add(cb_editAdditivityFlag);
        pa_edit.add(new SLabel("Appender reference:"));
        pa_edit.add(li_editAppenderRef);
        pa_edit.add(verticalSpace(0));
        pa_edit.add(bu_saveNode);

        SPanel pa_delete = createControlPanel("Delete selected category node:");
        pa_delete.add(bu_deleteNode);

        SPanel pa_commit = createControlPanel("Commit changes to 'log4j.xml':");
        pa_commit.add(bu_commitChanges);

        SLabel la_activityIndicator = new SLabel(" Loading data, wait!", new SURLIcon("../images/progress.gif"));
        la_activityIndicator.setName("ajaxActivityIndicator");

        SPanel pa_controls = new SPanel(new SBoxLayout(SBoxLayout.VERTICAL));
        pa_controls.setVerticalAlignment(SConstants.TOP_ALIGN);
        pa_controls.add(pa_edit);
        pa_controls.add(verticalSpace(10));
        pa_controls.add(pa_delete);
        pa_controls.add(verticalSpace(10));
        pa_controls.add(pa_commit);
        pa_controls.add(verticalSpace(10));
        pa_controls.add(la_activityIndicator);

        fo_form = new SForm(new SGridLayout(1, 2, 10, 10));
        fo_form.setStyle(fo_form.getStyle() + " fo_form");
        fo_form.add(sp_tree);
        fo_form.add(pa_controls);

        SLabel la_header = new SLabel(new SURLIcon("../images/header.jpg"));
        la_header.setStyle(la_header.getStyle() + " la_header");

        la_status = new SLabel();
        la_status.setStyle(la_status.getStyle() + " la_status");
        la_status.setPreferredSize(SDimension.FULLWIDTH);
        resetStatusLabel();

        SPanel pa_application = new SPanel(new SBoxLayout(SBoxLayout.VERTICAL));
        pa_application.setHorizontalAlignment(SConstants.CENTER_ALIGN);
        pa_application.setStyle(pa_application.getStyle() + " pa_application");
        pa_application.add(la_header);
        pa_application.add(fo_form);
        pa_application.add(la_status);

        // TESTING BACK BUTTON & HISTORY
        if (false) {
            fr_frame.setNoCaching(false);
            tr_domTree.setEpochCheckEnabled(false);
            fo_form.setPostMethod(false);
        }

        // TESTING ON-CHANGE-SUBMIT-LISTENERS
        if (false) {
            bg_insertOrUpdate.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    log.info("ActionListener of ButtonGroup!!!");
                }
            });
            cb_editAdditivityFlag.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    log.info("ItemListener of ComboBox!!!");
                }
            });
            li_editAppenderRef.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    log.info("ListSelectionListener of List!!!");
                }
            });
            tf_editCategoryName.addDocumentListener(new SDocumentListener() {
                public void changedUpdate(SDocumentEvent e) {
                    log.info("DocumentListener of TextField - changed!!!");
                }
                public void insertUpdate(SDocumentEvent e) {
                    log.info("DocumentListener of TextField - insert!!!");
                }
                public void removeUpdate(SDocumentEvent e) {
                    log.info("DocumentListener of TextField - remove!!!");
                }
            });
        }

        SContainer pa_content = fr_frame.getContentPane();
        pa_content.setStyle(pa_content.getStyle() + " pa_content");

        try {
            java.net.URL templateURL = SessionManager.getSession()
                .getServletContext().getResource("/templates/main.thtml");
            if (templateURL == null) {
                pa_content.add(new SLabel("Could not find template file!"));
                return;
            }
            pa_content.setLayout(new STemplateLayout(templateURL));
        } catch (java.io.IOException ex) {
            log.error("Could not find template file!", ex);
        }

        pa_content.add(pa_application, "application");
        pa_content.add(createAjaxDebuggingPanel(), "debugging");
        fr_frame.addHeader(new StyleSheetHeader("../css/custom.css"));
        fr_frame.setVisible(true);
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
            la_status.setText("  You have to provide at least a category name and a priority value!");
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
        bu_saveNode.setText("Insert");
        bu_saveNode.setIcon(INSERT_IMG);
        resetStatusLabel();
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
        bu_saveNode.setText("Update");
        bu_saveNode.setIcon(UPDATE_IMG);
        resetStatusLabel();
    }

    private void resetStatusLabel() {
        la_status.setText("  Usage: Insert, update or delete category nodes by means of the" +
                " according widgets - then commit all changes to 'log4j.xml'.");
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

    private SComponent verticalSpace(int height) {
        SLabel label = new SLabel();
        label.setAttribute(CSSProperty.HEIGHT, height + "px");
        return label;
    }

    private SPanel createControlPanel(String title) {
        SBoxLayout boxLayout = new SBoxLayout(SBoxLayout.VERTICAL);
        boxLayout.setHgap(20);
        boxLayout.setVgap(5);

        SPanel panel = new SPanel();
        panel.setLayout(boxLayout);
        panel.setStyle(panel.getStyle() + " pa_control");

        SLabel la_title = new SLabel(title);
        la_title.setStyle(la_title.getStyle() + " la_title");

        panel.add(la_title);
        panel.add(verticalSpace(0));
        return panel;
    }

    private SPanel createAjaxDebuggingPanel() {
        SBoxLayout boxLayout = new SBoxLayout(SBoxLayout.VERTICAL);
        boxLayout.setHgap(20);
        boxLayout.setVgap(5);

        SPanel pa_debug = new SPanel(boxLayout);
        pa_debug.setStyle(pa_debug.getStyle() + " pa_debug");
        pa_debug.setVerticalAlignment(SConstants.TOP_ALIGN);

        SLabel la_title = new SLabel("Playground for debugging AJAX:");
        la_title.setStyle(la_title.getStyle() + " la_title");
        pa_debug.add(la_title);

        boolean selected;
        final String[] cb_texts = {
                "Frame: updates enabled -> ",
                "Frame: cursor enabled -> ",
                "Form: reload forced -> ",
                "Tree: reload forced -> ",
                "Link: reload forced -> "
        };

        selected = fr_frame.isUpdateEnabled();
        final SCheckBox cb_toggleFrameUpdateEnabled =
            new SCheckBox(cb_texts[0] + selected, selected);
        cb_toggleFrameUpdateEnabled.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean state = fr_frame.isUpdateEnabled();
                fr_frame.setUpdateEnabled(!state);
                cb_toggleFrameUpdateEnabled.setText(cb_texts[0] + !state);
            }
        });

        Map<String, Object> updateCursor = fr_frame.getUpdateCursor();
        selected = ((Boolean) updateCursor.get("enabled")).booleanValue();
        final SCheckBox cb_toggleFrameUpdateCursor =
            new SCheckBox(cb_texts[1] + selected, selected);
        cb_toggleFrameUpdateCursor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Map<String, Object> updateCursor = fr_frame.getUpdateCursor();
                boolean state = ((Boolean) updateCursor.get("enabled")).booleanValue();
                updateCursor.put("enabled", !state);
                fr_frame.setUpdateCursor(updateCursor);
                cb_toggleFrameUpdateCursor.setText(cb_texts[1] + !state);
            }
        });

        selected = fo_form.isReloadForced();
        final SCheckBox cb_toggleFormReloadForced =
            new SCheckBox(cb_texts[2] + selected, selected);
        cb_toggleFormReloadForced.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean state = fo_form.isReloadForced();
                fo_form.setReloadForced(!state);
                cb_toggleFormReloadForced.setText(cb_texts[2]  + !state);
            }
        });

        selected = tr_domTree.isReloadForced();
        final SCheckBox cb_toggleTreeReloadForced =
            new SCheckBox(cb_texts[3] + selected, selected);
        cb_toggleTreeReloadForced.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean state = tr_domTree.isReloadForced();
                tr_domTree.setReloadForced(!state);
                cb_toggleTreeReloadForced.setText(cb_texts[3]  + !state);
            }
        });

        selected = cb_toggleTreeReloadForced.isReloadForced();
        final SCheckBox cb_toggleCheckboxTest =
            new SCheckBox(cb_texts[4] + selected, selected);
        cb_toggleCheckboxTest.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean state = cb_toggleTreeReloadForced.isReloadForced();
                cb_toggleTreeReloadForced.setReloadForced(!state);
                cb_toggleCheckboxTest.setText(cb_texts[4]  + !state);
            }
        });

        String[] scrollables = {"Scrollable: tree of log4j.xml",
                "Scrollable: table of random items", "Scrollable: list of random items"};
        final SComboBox cb_changeScrollable = new SComboBox(scrollables);
        cb_changeScrollable.setPreferredSize(IN_DIM);
        cb_changeScrollable.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int newPolicy = cb_changeScrollable.getSelectedIndex();
                switch (newPolicy) {
                case 0:
                    sp_tree.setViewportView(tr_domTree);
                    break;
                case 1:
                    sp_tree.setViewportView(createTestTable());
                    break;
                case 2:
                    sp_tree.setViewportView(createTestList());
                    break;
                }
            }
        });

        String[] scrollpaneModes = {"ScrollPane Mode: scrolling",
                "ScrollPane Mode: complete", "ScrollPane Mode: paging"};
        final SComboBox cb_changeScrollPaneMode = new SComboBox(scrollpaneModes);
        cb_changeScrollPaneMode.setPreferredSize(IN_DIM);
        cb_changeScrollPaneMode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int newMode = cb_changeScrollPaneMode.getSelectedIndex();
                switch (newMode) {
                case 0:
                    sp_tree.setMode(SScrollPane.MODE_SCROLLING);
                    sp_tree.setPreferredSize(new SDimension(477, SDimension.AUTO_INT));
                    break;
                case 1:
                    sp_tree.setMode(SScrollPane.MODE_COMPLETE);
                    sp_tree.setPreferredSize(new SDimension(477, 482));
                    break;
                case 2:
                    sp_tree.setMode(SScrollPane.MODE_PAGING);
                    sp_tree.setPreferredSize(new SDimension(477, SDimension.AUTO_INT));
                    break;
                }
            }
        });

        final SPageScroller horizontalPageScroller = new SPageScroller(SPageScroller.VERTICAL);
        horizontalPageScroller.setLayoutMode(SPageScroller.HORIZONTAL);
        String[] horizontalScrollbars = {"H-ScrollBar: scrollbar",
                "H-ScrollBar: pagescroller", "H-ScrollBar: null"};
        final SComboBox cb_changeHScrollBar = new SComboBox(horizontalScrollbars);
        cb_changeHScrollBar.setPreferredSize(IN_DIM);
        cb_changeHScrollBar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int newScrollbar = cb_changeHScrollBar.getSelectedIndex();
                switch (newScrollbar) {
                case 0:
                    sp_tree.setHorizontalScrollBar(new SScrollBar(SScrollBar.HORIZONTAL));
                    break;
                case 1:
                    sp_tree.setHorizontalScrollBar(horizontalPageScroller);
                    break;
                case 2:
                    sp_tree.setHorizontalScrollBar(null);
                    break;
                }
            }
        });

        final SPageScroller verticalPageScroller = new SPageScroller(SPageScroller.HORIZONTAL);
        verticalPageScroller.setLayoutMode(SPageScroller.VERTICAL);
        String[] verticalScrollbars = {"V-ScrollBar: scrollbar",
                "V-ScrollBar: pagescroller", "V-ScrollBar: null"};
        final SComboBox cb_changeVScrollBar = new SComboBox(verticalScrollbars);
        cb_changeVScrollBar.setPreferredSize(IN_DIM);
        cb_changeVScrollBar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int newScrollbar = cb_changeVScrollBar.getSelectedIndex();
                switch (newScrollbar) {
                case 0:
                    sp_tree.setVerticalScrollBar(new SScrollBar(SScrollBar.VERTICAL));
                    break;
                case 1:
                    sp_tree.setVerticalScrollBar(verticalPageScroller);
                    break;
                case 2:
                    sp_tree.setVerticalScrollBar(null);
                    break;
                }
            }
        });

        String[] horizontalPolicies = {"H-ScrollBar Policy: as needed",
                "H-ScrollBar Policy: always", "H-ScrollBar Policy: never"};
        final SComboBox cb_changeHScrollBarPolicy = new SComboBox(horizontalPolicies);
        cb_changeHScrollBarPolicy.setPreferredSize(IN_DIM);
        cb_changeHScrollBarPolicy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int newPolicy = cb_changeHScrollBarPolicy.getSelectedIndex();
                switch (newPolicy) {
                case 0:
                    sp_tree.setHorizontalScrollBarPolicy(SScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                    break;
                case 1:
                    sp_tree.setHorizontalScrollBarPolicy(SScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                    break;
                case 2:
                    sp_tree.setHorizontalScrollBarPolicy(SScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                    break;
                }
            }
        });

        String[] verticalPolicies = {"V-ScrollBar Policy: as needed",
                "V-ScrollBar Policy: always", "V-ScrollBar Policy: never"};
        final SComboBox cb_changeVScrollBarPolicy = new SComboBox(verticalPolicies);
        cb_changeVScrollBarPolicy.setPreferredSize(IN_DIM);
        cb_changeVScrollBarPolicy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int newPolicy = cb_changeVScrollBarPolicy.getSelectedIndex();
                switch (newPolicy) {
                case 0:
                    sp_tree.setVerticalScrollBarPolicy(SScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                    break;
                case 1:
                    sp_tree.setVerticalScrollBarPolicy(SScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                    break;
                case 2:
                    sp_tree.setVerticalScrollBarPolicy(SScrollPane.VERTICAL_SCROLLBAR_NEVER);
                    break;
                }
            }
        });

        final SButton bu_markFrameDirty = new SButton("Reload the entire frame / mark it dirty!");
        bu_markFrameDirty.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fr_frame.reload();
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

        final SButton bu_forceServerError = new SButton("Force an \"Internal Server Error (500)\"!");
        bu_forceServerError.setName("force_error");

        final SAnchor an_abortCurrentAjaxRequest = new SAnchor();
        an_abortCurrentAjaxRequest.addScriptListener(new JavaScriptListener(
                JavaScriptEvent.ON_CLICK,
                "wingS.ajax.abortRequest();" +
                "return false;"
        ));
        an_abortCurrentAjaxRequest.add(new SLabel("Abort the current AJAX request!"));

        final Script includeJs = new JavaScriptHeader("../js/include.js");
        final Script includeJs2 = new JavaScriptHeader("../js/include2.js");
        final Link includeCss = new StyleSheetHeader("../css/include.css");
        final Link includeCss2 = new StyleSheetHeader("../css/include2.css");
        final SButton bu_addScriptHeader = new SButton("Add a new script header and link header");
        bu_addScriptHeader.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fr_frame.addHeader(includeJs);
                fr_frame.addHeader(includeCss);
                fr_frame.addHeader(includeJs2);
                fr_frame.addHeader(includeCss2);
            }
        });

        final SButton bu_removeScriptHeader = new SButton("Remove script header and link header");
        bu_removeScriptHeader.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fr_frame.removeHeader(includeJs);
                fr_frame.removeHeader(includeCss);
                fr_frame.removeHeader(includeJs2);
                fr_frame.removeHeader(includeCss2);
            }
        });

        final SAnchor an_alertHello = new SAnchor();
        an_alertHello.addScriptListener(new JavaScriptListener(
                JavaScriptEvent.ON_CLICK,
                "alertHello();"
        ));
        an_alertHello.add(new SLabel("Call 'alertHello();' placed in '../js/custom.js'"));

        String text = "This TextArea is just for debugging the 'onChangeSubmit'-behavior." +
                " If you want to test this behavior for other components too, you have to" +
                " enable the according switches within the source code of this application.";
        final STextArea ta_testTextArea = new STextArea(text);
        ta_testTextArea.setRows(4);
        ta_testTextArea.setPreferredSize(IN_DIM);
        ta_testTextArea.setHorizontalAlignment(SConstants.LEFT_ALIGN);
        ta_testTextArea.addDocumentListener(new SDocumentListener() {
            public void changedUpdate(SDocumentEvent e) {
                log.info("DocumentListener of TextArea - changed!!!");
            }
            public void insertUpdate(SDocumentEvent e) {
                log.info("DocumentListener of TextArea - insert!!!");
            }
            public void removeUpdate(SDocumentEvent e) {
                log.info("DocumentListener of TextArea - remove!!!");
            }
        });

        final SButton bu_testWhatever = new SButton("TEST WHATEVER!");
        bu_testWhatever.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tf_editPriorityValue.setText(null);
                int[] rows = {1,5,2,4,7};
                tr_domTree.setSelectionRows(rows);
                la_status.setText("1");
                la_status.setText("4");
                la_status.setText("32");
                tr_domTree.setSelectionRow(0);
                tr_domTree.setSelectionRow(0);
                tr_domTree.setSelectionRow(8);
                ta_testTextArea.setEnabled(false);
                li_editAppenderRef.setEnabled(false);
                tf_editCategoryName.setEnabled(false);
            }
        });

        final SAnchor an_toggleAjaxDebugView = new SAnchor();
        an_toggleAjaxDebugView.addScriptListener(new JavaScriptListener(
                JavaScriptEvent.ON_CLICK, "toggleAjaxDebugView()",
                "function toggleAjaxDebugView() {\n" +
                "  if (wingS.ajax.isDebugViewVisible()) wingS.ajax.setDebugViewVisible(false);\n" +
                "  else wingS.ajax.setDebugViewVisible(true);\n" +
                "  return false;\n" +
                "}"
        ));
        an_toggleAjaxDebugView.add(new SLabel("Show / hide AJAX debugging view"));

        addToAjaxDebuggingPanel(pa_debug, verticalSpace(0));
        addToAjaxDebuggingPanel(pa_debug, cb_toggleFrameUpdateEnabled);
        addToAjaxDebuggingPanel(pa_debug, cb_toggleFrameUpdateCursor);
//        addToAjaxDebuggingPanel(pa_debug, cb_toggleFormReloadForced);
        addToAjaxDebuggingPanel(pa_debug, cb_toggleTreeReloadForced);
//        addToAjaxDebuggingPanel(pa_debug, cb_toggleCheckboxTest);

//        addToAjaxDebuggingPanel(pa_debug, verticalSpace(5));
//        addToAjaxDebuggingPanel(pa_debug, bu_markFrameDirty);
//        addToAjaxDebuggingPanel(pa_debug, bu_doSomethingSpecial);
//        addToAjaxDebuggingPanel(pa_debug, bu_forceServerError);
//        addToAjaxDebuggingPanel(pa_debug, an_abortCurrentAjaxRequest);
//        addToAjaxDebuggingPanel(pa_debug, bu_addScriptHeader);
//        addToAjaxDebuggingPanel(pa_debug, bu_removeScriptHeader);
//        addToAjaxDebuggingPanel(pa_debug, an_alertHello);

        addToAjaxDebuggingPanel(pa_debug, verticalSpace(5));
        addToAjaxDebuggingPanel(pa_debug, createRandomResultPanel());

//        addToAjaxDebuggingPanel(pa_debug, verticalSpace(5));
//        addToAjaxDebuggingPanel(pa_debug, createMenuTestPanel());

//        addToAjaxDebuggingPanel(pa_debug, verticalSpace(5));
//        addToAjaxDebuggingPanel(pa_debug, cb_changeScrollable);
//        addToAjaxDebuggingPanel(pa_debug, cb_changeScrollPaneMode);
//        addToAjaxDebuggingPanel(pa_debug, cb_changeHScrollBar);
//        addToAjaxDebuggingPanel(pa_debug, cb_changeVScrollBar);
//        addToAjaxDebuggingPanel(pa_debug, cb_changeHScrollBarPolicy);
//        addToAjaxDebuggingPanel(pa_debug, cb_changeVScrollBarPolicy);
//
//        addToAjaxDebuggingPanel(pa_debug, verticalSpace(5));
//        addToAjaxDebuggingPanel(pa_debug, ta_testTextArea);

//        addToAjaxDebuggingPanel(pa_debug, verticalSpace(5));
//        addToAjaxDebuggingPanel(pa_debug, bu_testWhatever);

        addToAjaxDebuggingPanel(pa_debug, verticalSpace(5));
        addToAjaxDebuggingPanel(pa_debug, an_toggleAjaxDebugView);

        return pa_debug;
    }

    private void addToAjaxDebuggingPanel(SPanel debug, SComponent component) {
        component.setHorizontalAlignment(SConstants.LEFT_ALIGN);
        component.setShowAsFormComponent(false);
        debug.add(component);
    }

    private SPanel createRandomResultPanel() {
        int links = 6;

        class RandomResultGenerator implements ActionListener {
            private SLabel label;
            private boolean sleep;

            public RandomResultGenerator(SLabel label, boolean sleep) {
                this.label = label;
                this.sleep = sleep;
            }

            public void actionPerformed(ActionEvent e) {
                if (sleep) {
                    int ms = new Random().nextInt(3001);
                    try {
                        Thread.sleep(ms);
                    } catch (InterruptedException e1) {}
                    label.setText(ms + " ms");
                } else {
                    int nr = new Random().nextInt(9000) + 1000;
                    label.setText(nr + "");
                }
            }
        }

        SPanel panel = new SPanel(new SGridLayout(links, 3));
        for (int i = 0; i < links; ++i) {
            SLabel label = new SLabel();
            SButton button = new SButton();
            button.setShowAsFormComponent(false);
            button.setHorizontalAlignment(SConstants.LEFT_ALIGN);
            if (i < links - 1) {
                button.setText("Sleep for a while!  >>");
                button.addActionListener(new RandomResultGenerator(label, true));
            } else {
                button.setText("Say any number!  >>");
                button.addActionListener(new RandomResultGenerator(label, false));
                panel.add(new SSpacer(0, 10));
                panel.add(new SSpacer(0, 5));
                panel.add(new SSpacer(0, 10));
            }
            panel.add(button);
            panel.add(new SSpacer(0, 5));
            panel.add(label);
        }
        return panel;
    }

    private STable createTestTable() {
        final int rows = 50;
        final int cols = new Random().nextInt(15);

        String[] colNames = new String[cols];
        for (int c = 0; c < cols; ++c) {
            colNames[c] = "Column " + (c + 1);
        }

        String[][] modelData = new String[rows][cols];
        for (int r = 0; r < rows; ++r) {
            for (int c = 0; c < cols; ++c) {
                modelData[r][c] = "Item " + (r + 1) + "/" + (c + 1);
            }
        }

        return new STable(new DefaultTableModel(modelData, colNames));
    }

    private SList createTestList() {
        final int rows = 50;

        String[] modelData = new String[rows];
        for (int i = 0; i < rows; ++i) {
            modelData[i] = "This is list item number " + (i + 1);
        }

        SList li_testList = new SList(new SDefaultListModel(modelData));
        li_testList.setShowAsFormComponent(false);
        li_testList.setSelectionMode(SList.SINGLE_SELECTION);

        return li_testList;
    }

    private SPanel createMenuTestPanel() {
        SPanel pa_menuTest = new SPanel(new SGridLayout(1));

        SMenuBar menuBar = new SMenuBar();

        // Test right aligned menu
        SMenu helpMenu = new SMenu("Test");
        SMenuItem item1 = new SMenuItem("Item 1");
        SMenuItem item2 = new SMenuItem("Item 1");
        helpMenu.add(item1);
        helpMenu.add(item2);
        menuBar.add(helpMenu);

        pa_menuTest.add(helpMenu);
//
//        SMenu aboutMenu = new SMenu("About");
//        aboutMenu.setHorizontalAlignment(RIGHT_ALIGN);
//        SMenuItem aboutMenuItem = new SMenuItem("About WingSet");
//        aboutMenuItem.addActionListener(menuItemListener);
//        aboutMenu.add(aboutMenuItem);
//        menuBar.add(aboutMenu);
//
//        return li_testList;
        return pa_menuTest;
    }

}