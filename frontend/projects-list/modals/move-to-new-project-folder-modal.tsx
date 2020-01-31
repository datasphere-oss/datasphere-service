import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3 move-item-modal move-to-new-project-folder-modal\" auto-size=\"false\">
    +             <DkuModalHeader modal-class=\"has-border\">
    +                 <DkuModalTitle>移动到 {moveItemCountText()} 新文件夹</DkuModalTitle>
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
    +                 </form>
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