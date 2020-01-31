import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3\">
    +             <div dku-modal-header=\"\" modal-title={title}>
    +                 <form name=\"renameForm\" className=\"dkuform-horizontal\">
    +                     <div className=\"modal-body\" ng-switch=\"options.type\">
    +                         <div className=\"control-group\" ng-switch-default=\"\">
    +                             {/* new scope => using $parent.value */}
    +                             <label className=\"control-label\" htmlFor=\"promptInput\">
    +                                 {text}
    +                             </label>
    +                             <div className=\"controls\">
    +                                 <input
    +                                     id=\"promptInput\"
    +                                     type={options.type || 'text'}
    +                                     title={options.title}
    +                                     placeholder={options.placeholder}
    +                                     value={$parent.value}
    +                                     pattern=\"options.pattern\"
    +                                     required={true}
    +                                     className=\"input-xlarge\"
    +                                     name=\"newName\"
    +                                 />
    +                             </div>
    +                         </div>
    +                         <div ng-switch-when=\"textarea\">
    +                             <label
    +                                 className=\"control-label\"
    +                                 htmlFor=\"promptInput\"
    +                                 style=\"width: 100%; text-align: left\">
    +                                 {text}
    +                             </label>
    +                             <textarea
    +                                 id=\"promptInput\"
    +                                 style=\"width: 100%\"
    +                                 rows={options.rows || '4'}
    +                                 title={options.title}
    +                                 placeholder={options.placeholder}
    +                                 value={$parent.value}
    +                                 required={true}
    +                                 className=\"input-xlarge\"
    +                                 name=\"newName\"
    +                             />
    +                         </div>
    +                     </div>
    +                     <div
    +                         className=\"modal-footer modal-footer-std-buttons\"
    +                         global-keydown=\"{'enter':'confirm();', 'esc': 'cancel();' }\">
    +                         <button
    +                             type=\"button\"
    +                             className=\"btn btn-default\"
    +                             onClick={() => {
    +                                 cancel();
    +                             }}>
    +                             取消
    +                         </button>
    +                         <input
    +                             type=\"submit\"
    +                             className=\"btn btn-primary\"
    +                             onClick={() => {
    +                                 confirm();
    +                             }}
    +                             disabled={renameForm.$invalid}
    +                             value=\"OK\"
    +                         />
    +                     </div>
    +                 </form>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;