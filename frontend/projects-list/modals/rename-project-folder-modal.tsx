import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3 rename-project-folder-modal\" auto-size=\"false\">
    +             <DkuModalHeader modal-class=\"has-border\">
    +                 <DkuModalTitle>重命名文件夹 \"{folder.name}\"</DkuModalTitle>
    +             </DkuModalHeader>
    + 
    +             <div className=\"modal-body\">
    +                 <div block-api-error=\"\">
    +                     <form
    +                         className=\"dkuform-modal-horizontal\"
    +                         name=\"renameProjectFolderForm\"
    +                         style=\"margin-bottom: 0px;\">
    +                         <div className=\"control-group\" style=\"margin-bottom: 0px;\">
    +                             <label htmlFor=\"newName\" className=\"control-label\">
    +                                 名称
    +                             </label>
    +                             <div className=\"controls\">
    +                                 <input
    +                                     type=\"text\"
    +                                     id=\"newName\"
    +                                     name=\"newName\"
    +                                     placeholder=\"Folder name\"
    +                                     value={newName}
    +                                     auto-focus=\"true\"
    +                                     className=\"{'ng-invalid' : !isNameValid(renameProjectFolderForm.newName, parentPath, true)}\"
    +                                 />
    +                             </div>
    +                         </div>
    + 
    +                         {!isNameUniqueInFolder(newName, parentPath) && newName != folder.name ? (
    +                             <div className=\"alert alert-error\" style=\"margin-top: 20px;\">
    +                                 <i className=\"icon-warning-sign\">
    +                                     {' '}
    +                                     已经有文件夹名为 {newName} 在 {parentPath}.
    +                                 </i>
    +                             </div>
    +                         ) : null}
    +                         <i className=\"icon-warning-sign\" />
    +                     </form>
    +                 </div>
    +                 <i className=\"icon-warning-sign\">
    +                     <div className=\"modal-footer modal-footer-std-buttons has-border\">
    +                         <button
    +                             type=\"button\"
    +                             className=\"btn btn-default\"
    +                             onClick={() => {
    +                                 dismiss();
    +                             }}>
    +                             取消
    +                         </button>
    +                         <button
    +                             type=\"submit\"
    +                             className=\"btn btn-primary\"
    +                             onClick={() => {
    +                                 confirm();
    +                             }}
    +                             disabled={!isNameValid(renameProjectFolderForm.newName, parentPath, false)}>
    +                             重命名
    +                         </button>
    +                     </div>
    +                 </i>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;