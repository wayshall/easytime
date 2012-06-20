package org.onetwo.eclipse.codegen.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;
import org.onetwo.core.util.StringUtils;
import org.onetwo.eclipse.codegen.CodegenPlugin;
import org.onetwo.eclipse.codegen.Constant;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class TableView extends ViewPart {
	private TreeViewer viewer;
	private DrillDownAdapter drillDownAdapter;
	private Action refreshTableAction;
	private Action geranteCodeAction;
	private Action doubleClickAction;
	
	private Action addDatabaseAction;
	
	private TreeParent rootNode;

	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */
	 
	class TreeObject implements IAdaptable {
		private String name;
		private TreeParent parent;
		
		public TreeObject(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public void setParent(TreeParent parent) {
			this.parent = parent;
		}
		public TreeParent getParent() {
			return parent;
		}
		public String toString() {
			return getName();
		}
		public Object getAdapter(Class key) {
			return null;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
	
	class TreeParent extends TreeObject {
		private String id;
		private ArrayList children;
		

		public TreeParent(String name) {
			this(Messages.getString("TableView.0"), name); //$NON-NLS-1$
		}
		public TreeParent(String id, String name) {
			super(name);
			this.id = id;
			children = new ArrayList();
		}
		public void addChild(TreeObject child) {
			children.add(child);
			child.setParent(this);
		}
		public void removeChild(TreeObject child) {
			children.remove(child);
			child.setParent(null);
		}
		public TreeObject [] getChildren() {
			return (TreeObject [])children.toArray(new TreeObject[children.size()]);
		}
		public boolean hasChildren() {
			return children.size()>0;
		}
		public String getId() {
			return id;
		}
		
	}

	class ViewContentProvider implements IStructuredContentProvider, 
										   ITreeContentProvider {
		private TreeParent invisibleRoot;

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			if (parent.equals(getViewSite())) {
				if (invisibleRoot==null) initialize();
				return getChildren(invisibleRoot);
			}
			return getChildren(parent);
		}
		public Object getParent(Object child) {
			if (child instanceof TreeObject) {
				return ((TreeObject)child).getParent();
			}
			return null;
		}
		public Object [] getChildren(Object parent) {
			if (parent instanceof TreeParent) {
				return ((TreeParent)parent).getChildren();
			}
			return new Object[0];
		}
		public boolean hasChildren(Object parent) {
			if (parent instanceof TreeParent)
				return ((TreeParent)parent).hasChildren();
			return false;
		}
/*
 * We will set up a dummy model to initialize tree heararchy.
 * In a real code, you will connect to a real model and
 * expose its hierarchy.
 */
		private void initialize() {
			
			rootNode = new TreeParent(Messages.getString("TableView.1"), Messages.getString("TableView.2")); //$NON-NLS-1$ //$NON-NLS-2$
			
			invisibleRoot = new TreeParent(Messages.getString("TableView.3")); //$NON-NLS-1$
			invisibleRoot.addChild(rootNode);
		}
	}
	class ViewLabelProvider extends LabelProvider {

		public String getText(Object obj) {
			return obj.toString();
		}
		public Image getImage(Object obj) {
			String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
			if (obj instanceof TreeParent)
			   imageKey = ISharedImages.IMG_OBJ_FOLDER;
			return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
		}
	}
	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public TableView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		drillDownAdapter = new DrillDownAdapter(viewer);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager(Messages.getString("TableView.4")); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				TableView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(refreshTableAction);
		manager.add(new Separator());
		manager.add(geranteCodeAction);
	}

	private void fillContextMenu(IMenuManager manager) {
		ITreeSelection selection = (ITreeSelection) viewer.getSelection();
		Object first = selection.getFirstElement();
		if(first!=null &&first.equals(rootNode))
			manager.add(refreshTableAction);
		else
			manager.add(geranteCodeAction);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(addDatabaseAction);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
	}

	private void makeActions() {
		this.addDatabaseAction = new Action(){

			@Override
			public void run() {
				Shell shell = new Shell();
				PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(shell, Constant.preferencePageId, null, null);
				dialog.open();
			}
			
		};
		addDatabaseAction.setText(Messages.getString("TableView.5")); //$NON-NLS-1$
		addDatabaseAction.setToolTipText(Messages.getString("TableView.6")); //$NON-NLS-1$
		addDatabaseAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
		refreshTableAction = new Action() {
			public void run() {
				fetchTablesToNode(rootNode, true);
			}
		};
		refreshTableAction.setText(Messages.getString("TableView.7")); //$NON-NLS-1$
		refreshTableAction.setToolTipText(Messages.getString("TableView.8")); //$NON-NLS-1$
		refreshTableAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
		geranteCodeAction = new Action() {
			public void run() {
				ITreeSelection selection = (ITreeSelection) viewer.getSelection();
				if(selection.isEmpty()){
					showMessage(Messages.getString("TableView.9")); //$NON-NLS-1$
					return ;
				}
				List tableObjects = selection.toList();
				if(tableObjects.contains(rootNode)){
					showMessage(Messages.getString("TableView.10")); //$NON-NLS-1$
					return ;
				}
				GenConfirmDialog a = new GenConfirmDialog(viewer.getControl().getShell(), tableObjects);
				a.open();
			}
		};
		geranteCodeAction.setText(Messages.getString("TableView.11")); //$NON-NLS-1$
		geranteCodeAction.setToolTipText(Messages.getString("TableView.12")); //$NON-NLS-1$
		geranteCodeAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				if(obj instanceof TreeParent){
					TreeParent root = (TreeParent) obj;
					if(rootNode.getId().equals(root.getId())){
						rootNode.setName("请稍后……");
						viewer.refresh();
						fetchTablesToNode(root, false);
					}
				}
			}
		};
	}
	
	protected void fetchTablesToNode(TreeParent root, boolean refresh){
		String dbname = CodegenPlugin.getCodegenFacade().getDatabaseName();
		if(StringUtils.isNotBlank(dbname))
			root.setName(dbname);
		Collection<String> tables = CodegenPlugin.getCodegenFacade().getTableNames(refresh);
		if(tables==null){
			showMessage(Messages.getString("TableView.13")); //$NON-NLS-1$
			return ;
		}
		root.children.clear();
		this.viewer.refresh();
		
		TreeObject c = null;
		for(String t : tables){
			c = new TreeObject(t);
			root.addChild(c);
		}
		this.viewer.refresh();
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				try {
					doubleClickAction.run();
				} catch (Exception e) {
					MessageDialog.openInformation(new Shell(), Messages.getString("TableView.16"), Messages.getString("TableView.17")+e.getMessage());
				}
			}
		});
	}
	private void showMessage(String message) {
		MessageDialog.openInformation(
			viewer.getControl().getShell(),
			Messages.getString("TableView.14"), //$NON-NLS-1$
			message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}