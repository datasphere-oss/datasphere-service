import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"h100 runnable-page\">
    +             <div className=\"h100 oa horizontal-centerer\">
    +                 <div className=\"recipe-settings-section1 w800\">
    +                     <h1 className=\"recipe-settings-section1-title\">Security</h1>
    +                     <form className=\"dkuform-horizontal recipe-settings-section2\">
    +                         <div className=\"control-group\">
    +                             <label className=\"control-label\">Run as</label>
    +                             {allUsers ? (
    +                                 <div className=\"controls\">
    +                                     <select
    +                                         dku-bs-select=\"\"
    +                                         value={insight.params.runAsUser}
    +                                         ng-options=\"user.login as user.login for user in allUsers\">
    +                                         <option value=\"\">Current user</option>
    +                                     </select>
    +                                 </div>
    +                             ) : null}
    +                         </div>
    +                     </form>
    +                 </div>
    +                 {!hasSettings ? (
    +                     <div className=\"recipe-settings-section1 w800\">
    +                         <h1 className=\"recipe-settings-section1-title\">No parameters</h1>
    +                     </div>
    +                 ) : null}
    +                 {hasSettings ? (
    +                     <div className=\"recipe-settings-section1 w800\">
    +                         <h1 className=\"recipe-settings-section1-title\">Parameters</h1>
    +                         <form>
    +                             <div
    +                                 plugin-settings-alert=\"\"
    +                                 component-type=\"recipe\"
    +                                 has-settings=\"pluginDesc.hasSettings\"
    +                                 app-config=\"appConfig\">
    +                                 {(desc.params && desc.params.length > 0) ||
    +                                 (desc.paramsTemplate && desc.paramsTemplate.length > 0) ? (
    +                                     <div
    +                                         custom-params-form=\"\"
    +                                         desc=\"desc\"
    +                                         plugin-desc=\"pluginDesc\"
    +                                         component-id=\"runnable.id\"
    +                                         config=\"insight.params.config\"
    +                                     />
    +                                 ) : null}
    + 
    +                                 <div className=\"dkuform-horizontal\" style=\"padding: 0 10px;\">
    +                                     <div className=\"control-group\">
    +                                         <label className=\"control-label\">重新设置到默认</label>
    +                                         <div className=\"controls\">
    +                                             <button
    +                                                 type=\"button\"
    +                                                 className=\"btn btn-default\"
    +                                                 onClick={() => {
    +                                                     resetSettings();
    +                                                 }}>
    +                                                 Reset
    +                                             </button>
    +                                         </div>
    +                                     </div>
    +                                 </div>
    +                             </div>
    +                         </form>
    +                     </div>
    +                 ) : null}
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;