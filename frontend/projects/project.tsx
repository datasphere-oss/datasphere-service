import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"h100\">
    +             {topNav.tabsType == 'DASHBOARD' ? (
    +                 <div className=\"top-level-tabs objecttype-project\">
    +                     <div className=\"row-fluid object-nav\">
    +                         <div className=\"pull-right\">
    +                             <div className=\"otherLinks\">
    +                                 {dashboardContext.writePermission ? (
    +                                     <NavLink
    +                                         style=\"display: inline-block; vertical-align: top\"
    +                                         to=\"projects.project.insights.list\"
    +                                         className=\"btn btn-default\">
    +                                         {' '}
    +                                         <i className=\"icon-insight\" />
    +                                         &nbsp; 管理洞察
    +                                     </NavLink>
    +                                 ) : null}
    +                                 {!dashboardContext.readOnly && dashboardContext.writePermission ? (
    +                                     <div style=\"display: inline-block\">
    +                                         <button
    +                                             onClick={() => {
    +                                                 dashboardContext.onCreateSection();
    +                                             }}
    +                                             className=\"btn if-writable btn-create-section\">
    +                                             {' '}
    +                                             <i className=\"icon-plus\" />
    +                                             &nbsp; 创建章节
    +                                         </button>
    +                                         <button
    +                                             className=\"btn btn-success\"
    +                                             onClick={() => {
    +                                                 dashboardContext.readOnly = true;
    +                                             }}
    +                                             style=\"width:130px;float:right\">
    +                                             <i className=\"icon-lock\" />
    +                                             &nbsp; 完成编辑
    +                                         </button>
    +                                     </div>
    +                                 ) : null}
    + 
    +                                 {dashboardContext.readOnly && dashboardContext.writePermission ? (
    +                                     <div style=\"display: inline-block;  vertical-align: top\">
    +                                         <button
    +                                             className=\"btn btn-success\"
    +                                             onClick={() => {
    +                                                 dashboardContext.readOnly = false;
    +                                             }}
    +                                             style=\"width:130px; float:right\">
    +                                             <i className=\"icon-pencil\">&nbsp; 编辑</i>
    +                                         </button>
    +                                         <i className=\"icon-pencil\" />
    +                                     </div>
    +                                 ) : null}
    +                                 <i className=\"icon-pencil\" />
    +                             </div>
    +                             <i className=\"icon-pencil\" />
    +                         </div>
    +                         <i className=\"icon-pencil\" />
    +                     </div>
    +                     <i className=\"icon-pencil\" />
    +                 </div>
    +             ) : null}
    +             <i className=\"icon-pencil\">
    +                 <div ui-view=\"\" className=\"h100\" />
    +             </i>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;