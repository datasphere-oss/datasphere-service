import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"h100 managed-folder-view\">
    +             <div block-api-error=\"\" />
    +             {insight.params.filePath == null || insight.params.isDirectory ? (
    +                 <div
    +                     className=\"h100\"
    +                     managed-folder-contents-view=\"\"
    +                     odb=\"folder\"
    +                     read-only=\"true\"
    +                     can-download=\"folder.canDownload\"
    +                     sub-folder-starting-point=\"insight.params.filePath\">
    +                     {insight.params.filePath != null && !insight.params.isDirectory ? (
    +                         <div className=\"h100 vertical-flex content-preview-wrapper\">
    +                             <div
    +                                 className=\"noflex\"
    +                                 managed-folder-preview-header=\"\"
    +                                 previewed-item=\"previewedItem\"
    +                                 odb=\"folder\"
    +                                 read-only=\"true\"
    +                                 can-download=\"folder.canDownload\">
    +                                 <div
    +                                     className=\"flex\"
    +                                     managed-folder-contents-preview=\"\"
    +                                     previewed-item=\"previewedItem\"
    +                                     odb=\"folder\"
    +                                 />
    +                             </div>
    +                         </div>
    +                     ) : null}
    +                 </div>
    +             ) : null}
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;