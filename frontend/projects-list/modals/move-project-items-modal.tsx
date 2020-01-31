import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3 move-item-modal move-project-modal\" auto-size=\"false\">
    +             <DkuModalHeader>
    +                 <DkuModalTitle>
    +                     移动
    +                     {folderPaths.length == 0 ? (
    +                         <span>
    +                             {projectKeys.length > 1 ? <span>{projectKeys.length}</span> : null}
    +                             {plurify('project', projectKeys.length)}
    +                             {projectKeys.length == 1 ? <span>\"{projectsMap[projectKeys[0]].name}\"</span> : null}
    +                         </span>
    +                     ) : null}
    +                     {projectKeys.length == 0 ? (
    +                         <span>
    +                             {folderPaths.length > 1 ? <span>{folderPaths.length}</span> : null}
    +                             {plurify('folder', folderPaths.length)}
    +                             {folderPaths.length == 1 ? <span>\"{getFolderName(folderPaths[0])}\"</span> : null}
    +                         </span>
    +                     ) : null}
    +                     {projectKeys.length > 0 && folderPaths.length > 0 ? (
    +                         <span>{projectKeys.length + folderPaths.length} items</span>
    +                     ) : null}
    +                 </DkuModalTitle>
    +             </DkuModalHeader>
    +             <div className=\"modal-body vertical-flex\">
    +                 <div block-api-error=\"\" className=\"noflex\" />
    +                 <div className=\"flex\">
    +                     <div className=\"fh\">
    +                         <BrowsePath
    +                             className=\"h100 vertical-flex\"
    +                             browse-fn=\"browse\"
    +                             title=\"projects\"
    +                             path=\"parentPath\"
    +                             can-select-fn=\"canSelect\"
    +                             can-browse-fn=\"canBrowse\"
    +                             destination-label=\"to\"
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
    +                         className=\"btn btn-primary\"
    +                         onClick={() => {
    +                             confirm();
    +                         }}>
    +                         移动
    +                     </button>
    +                 </div>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;