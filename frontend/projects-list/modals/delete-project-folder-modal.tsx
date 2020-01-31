import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3 move-item-modal delete-project-folder-modal\" auto-size=\"false\">
    +             <DkuModalHeader modal-class=\"has-border\">
    +                 <DkuModalTitle>
    +                     <span>
    +                         删除 {folderPaths.length > 1 ? <span>{folderPaths.length}</span> : null}{' '}
    +                         {plurify('folder', folderPaths.length)}{' '}
    +                         {folderPaths.length == 1 ? <span>\"{getFolderName(folderPaths[0])}\"</span> : null}
    +                     </span>
    +                 </DkuModalTitle>
    +             </DkuModalHeader>
    +             <div className=\"modal-body vertical-flex\">
    +                 <div block-api-error=\"\" className=\"noflex\" />
    +                 <div className=\"alert alert-danger instructions\">
    +                     文件夹包含
    +                     {containedProjects.length > 0 ? (
    +                         <span>
    +                             {containedProjects.length} {plurify('project', containedProjects.length)}
    +                         </span>
    +                     ) : null}
    +                     {containedProjects.length > 0 && containedFolders.length > 0 ? <span> 同时 </span> : null}
    +                     {containedFolders.length > 0 ? (
    +                         <span>
    +                             {containedFolders.length} {plurify('folder', containedFolders.length)}
    +                         </span>
    +                     ) : null}. 定义一个新位置保存{containedProjects.length + containedFolders.length > 1 ? (
    +                         <span>它们</span>
    +                     ) : null}
    +                     {containedProjects.length + containedFolders.length == 1 ? <span>it</span> : null}.
    +                 </div>
    +                 <div className=\"flex\">
    +                     <div className=\"fh\">
    +                         <BrowsePath
    +                             className=\"h100 vertical-flex\"
    +                             browse-fn=\"browse\"
    +                             title=\"projects\"
    +                             path=\"parentPath\"
    +                             can-select-fn=\"canSelect\"
    +                             can-browse-fn=\"canBrowse\"
    +                         />
    +                     </div>
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
    +                         className=\"btn btn-danger\"
    +                         onClick={() => {
    +                             confirm();
    +                         }}>
    +                         删除
    +                     </button>
    +                 </div>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;