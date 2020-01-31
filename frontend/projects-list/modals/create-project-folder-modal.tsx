import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3 move-item-modal create-project-folder-modal\" auto-size=\"false\">
    +             <DkuModalHeader modal-class=\"has-border\">
    +                 <DkuModalTitle>创建文件夹</DkuModalTitle>
    +             </DkuModalHeader>
    +             <div className=\"modal-body vertical-flex\">
    +                 <div block-api-error=\"\" className=\"noflex\" />
    +                 <form name=\"newFolderForm\" className=\"dkuform-modal-horizontal\">
    +                     <div className=\"control-group\">
    +                         <label htmlFor=\"folderName\" className=\"control-label\">
    +                             名称
    +                         </label>
    +                         <div className=\"controls\">
    +                             <input
    +                                 type=\"text\"
    +                                 id=\"folderName\"
    +                                 name=\"name\"
    +                                 placeholder=\"New folder name\"
    +                                 value={newFolder.name}
    +                                 className=\"{'ng-invalid' : !isNameValid(newFolderForm.name, parentPath, true)}\"
    +                                 auto-focus=\"true\"
    +                             />
    +                         </div>
    +                     </div>
    +                     <div className=\"control-group\">
    +                         <label className=\"control-label\">选择内容</label>
    +                     </div>
    +                 </form>
    +                 <div className=\"flex\">
    +                     <div className=\"fh\">
    +                         <BrowsePath
    +                             className=\"h100 vertical-flex\"
    +                             browse-fn=\"browse\"
    +                             title=\"projects\"
    +                             path=\"parentPath\"
    +                             selected-items=\"newFolder.content\"
    +                             can-select-fn=\"canSelect\"
    +                             can-browse-fn=\"canBrowse\"
    +                             is-multi-select=\"true\"
    +                         />
    +                     </div>
    +                 </div>
    +                 <div className=\"warning-wrapper\">
    +                     <div className=\"alert alert-warning\">文件夹不为空, 请选择至少一个条目放入.</div>
    +                 </div>
    +             </div>
    +             <div className=\"modal-footer modal-footer-std-buttons has-border\">
    +                 <div className=\"pull-right\">
    +                     <button
    +                         type=\"button\"
    +                         className=\"btn btn-default\"
    +                         onClick={() => {
    +                             dismiss();
    +                         }}>
    +                         取消
    +                     </button>
    +                     <button
    +                         type=\"submit\"
    +                         className=\"btn btn-primary\"
    +                         onClick={() => {
    +                             confirm();
    +                         }}
    +                         disabled={!canConfirm()}>
    +                         创建
    +                     </button>
    +                 </div>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;