import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div>
    +             <div className=\"horizontal-flex row-fluid object-nav\">
    +                 <div className=\"flex oh\" std-object-breadcrumb=\"\">
    +                     <div className=\"noflex\">
    +                         <NavLink
    +                             className=\"{'enabled': topNav.tab == 'summary'}\"
    +                             to=\"projects.project.home.regular\"
    +                             fw500-width=\"\">
    +                             概要
    +                         </NavLink>
    +                         {isProjectAnalystRO() ? (
    +                             <NavLink
    +                                 className=\"{'enabled': topNav.tab == 'activity'}\"
    +                                 to=\"projects.project.home.activity\"
    +                                 fw500-width=\"\">
    +                                 活动
    +                             </NavLink>
    +                         ) : null}
    +                         {isProjectAnalystRO() ? (
    +                             <NavLink
    +                                 className=\"{'enabled': topNav.tab == 'changes'}\"
    +                                 to=\"projects.project.home.changes\"
    +                                 fw500-width=\"\">
    +                                 变更
    +                             </NavLink>
    +                         ) : null}
    +                         {isProjectAnalystRO() ? (
    +                             <NavLink
    +                                 className=\"{'enabled': topNav.tab == 'status'}\"
    +                                 to=\"projects.project.home.status.metrics\"
    +                                 fw500-width=\"\">
    +                                 指标
    +                             </NavLink>
    +                         ) : null}
    +                         <div className=\"otherLinks\">
    +                             <div discussions-button=\"\">
    +                                 <div className=\"dib\" include-no-scope=\"/templates/projects/actions.html\" />
    +                             </div>
    +                         </div>
    + 
    +                         <div ui-view=\"\" />
    +                         <div coachmark-serie-caller=\"\" serie-id=\"'project-home'\" />
    +                     </div>
    +                 </div>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;