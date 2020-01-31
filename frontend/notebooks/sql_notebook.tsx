import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div
    +             global-keydown=\"{ 'ctrl-enter meta-enter' : 'run(false)'}\"
    +             className=\"sql-notebook sql-editor\"
    +             navigator-object=\"\">
    +             <div className=\"object-nav horizontal-flex\">
    +                 <div std-object-breadcrumb=\"\" className=\"flex oh\">
    +                     <div className=\"noflex\">
    +                         <a
    +                             fw500-width=\"\"
    +                             className=\"{enabled: topNav.tab == 'summary'}\"
    +                             onClick={() => {
    +                                 topNav.tab = 'summary';
    +                             }}>
    +                             概要
    +                         </a>
    +                         <a
    +                             fw500-width=\"\"
    +                             className=\"{enabled: topNav.tab == 'query'}\"
    +                             onClick={() => {
    +                                 topNav.tab = 'query';
    +                             }}>
    +                             查询
    +                         </a>
    + 
    +                         <div className=\"otherLinks\">
    +                             <div discussions-button=\"\">
    +                                 <a className=\"btn btn-primary\" disabled={true}>
    +                                     自动保存
    +                                 </a>
    + 
    +                                 <div className=\"dropdown\">
    +                                     <button className=\"btn btn-action dropdown-toggle\" data-toggle=\"dropdown\" href=\"\">
    +                                         操作 <i className=\"icon-caret-down\" />
    +                                     </button>
    +                                     <i className=\"icon-caret-down\">
    +                                         <ul className=\"dropdown-menu pull-right text-left\">
    +                                             <li>
    +                                                 <a
    +                                                     onClick={() => {
    +                                                         duplicateNotebook();
    +                                                     }}>
    +                                                     <i className=\"icon-copy\"> 拷贝此脚本</i>
    +                                                 </a>
    +                                                 <i className=\"icon-copy\" />
    +                                             </li>
    +                                             <i className=\"icon-copy\">
    +                                                 <li>
    +                                                     <a
    +                                                         onClick={() => {
    +                                                             GlobalProjectActions.deleteTaggableObject(
    +                                                                 this,
    +                                                                 'SQL_NOTEBOOK',
    +                                                                 notebookParams.id,
    +                                                                 notebookParams.name
    +                                                             );
    +                                                         }}
    +                                                         className=\"text-error\">
    +                                                         <span className=\"text-error\">
    +                                                             <i className=\"icon-trash\"> 删除此脚本</i>
    +                                                         </span>
    +                                                         <i className=\"icon-trash\" />
    +                                                     </a>
    +                                                     <i className=\"icon-trash\" />
    +                                                 </li>
    +                                                 <i className=\"icon-trash\" />
    +                                             </i>
    +                                         </ul>
    +                                         <i className=\"icon-copy\">
    +                                             <i className=\"icon-trash\" />
    +                                         </i>
    +                                     </i>
    +                                 </div>
    +                                 <i className=\"icon-caret-down\">
    +                                     <i className=\"icon-copy\">
    +                                         <i className=\"icon-trash\" />
    +                                     </i>
    +                                 </i>
    +                             </div>
    +                             <i className=\"icon-caret-down\">
    +                                 <i className=\"icon-copy\">
    +                                     <i className=\"icon-trash\" />
    +                                 </i>
    +                             </i>
    +                         </div>
    +                         <i className=\"icon-caret-down\">
    +                             <i className=\"icon-copy\">
    +                                 <i className=\"icon-trash\" />
    +                             </i>
    +                         </i>
    +                     </div>
    +                     <i className=\"icon-caret-down\">
    +                         <i className=\"icon-copy\">
    +                             <i className=\"icon-trash\">
    +                                 {topNav.tab == 'summary' ? (
    +                                     <div className=\"dss-page\">
    +                                         <div include-no-scope=\"/templates/notebooks/sql-notebook-summary-tab.html\" />
    +                                     </div>
    +                                 ) : null}
    + 
    +                                 <div
    +                                     className=\"dss-page page-top-padding\"
    +                                     dku-if=\"topNav.tab == 'query'\"
    +                                     style=\"padding:0\">
    +                                     <div include-no-scope=\"/templates/notebooks/sql-notebook-query.html\" />
    +                                 </div>
    +                             </i>
    +                         </i>
    +                     </i>
    +                 </div>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;