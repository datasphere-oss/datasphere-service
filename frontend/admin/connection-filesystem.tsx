import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div ng-controller=\"FilesystemConnectionController\" className=\"h100 vertical-flex\">
    +             <div className=\"flex\">
    +                 <div className=\"fh oa\">
    +                     <div className=\"h100 small-lr-padding\">
    +                         <div className=\"generic-white-box\">
    +                             <div include-no-scope=\"/templates/admin/fragments/connection-name.html\">
    +                                 <div block-api-error=\"\">
    +                                     <form name=\"connectionParamsForm\" className=\"dkuform-horizontal\">
    +                                         <h2 className=\"settings-section-title\">位置</h2>
    +                                         <div
    +                                             className=\"control-group\"
    +                                             form-template-element=\"\"
    +                                             model=\"connection.params\"
    +                                             field=\"{name:'root',type:'string', mandatory:true, label:'根路径'}\">
    +                                             <div include-no-scope=\"/templates/admin/fragments/fs-naming-rule.html\">
    +                                                 <div include-no-scope=\"/templates/admin/fragments/connection-flags.html\" />
    +                                             </div>
    +                                         </div>
    +                                     </form>
    +                                 </div>
    +                                 <div
    +                                     className=\"noflex small-lr-padding page-top-padding\"
    +                                     include-no-scope=\"/templates/admin/fragments/connection-name-test-save-with-results.html\"
    +                                 />
    +                             </div>
    +                         </div>
    +                     </div>
    +                 </div>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;