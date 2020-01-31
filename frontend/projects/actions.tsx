import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return isProjectAdmin() ? (
    +         <div className=\"dropdown dib\">
    +             <button
    +                 className=\"btn btn-small btn-action dropdown-toggle\"
    +                 data-toggle=\"dropdown\"
    +                 id=\"qa_generic_actions-dropdown\"
    +                 href=\"\">
    +                 操作 <i className=\"icon-caret-down\" />
    +             </button>
    +             <i className=\"icon-caret-down\">
    +                 <ul className=\"dropdown-menu pull-right text-left\">
    +                     <li>
    +                         <a
    +                             onClick={() => {
    +                                 exportThisProject()();
    +                             }}>
    +                             <i className=\"icon-download-alt\">导出此工程</i>
    +                         </a>
    +                         <i className=\"icon-download-alt\" />
    +                     </li>
    +                     <i className=\"icon-download-alt\">
    +                         <li className=\"separator\" />
    +                         <li>
    +                             <a
    +                                 onClick={() => {
    +                                     deleteThisProject();
    +                                 }}
    +                                 id=\"qa_project-home_delete-project\">
    +                                 <span className=\"text-error\">
    +                                     <i className=\"icon-trash\">&nbsp; 删除此工程</i>
    +                                 </span>
    +                                 <i className=\"icon-trash\" />
    +                             </a>
    +                             <i className=\"icon-trash\" />
    +                         </li>
    +                         <i className=\"icon-trash\" />
    +                     </i>
    +                 </ul>
    +                 <i className=\"icon-download-alt\">
    +                     <i className=\"icon-trash\" />
    +                 </i>
    +             </i>
    +         </div>
    +     ) : null;
    + };
    + 
    + export default TestComponent;