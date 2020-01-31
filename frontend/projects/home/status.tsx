import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"dss-page\" ng-init=\"hasNoMetricsSettings=true;\">
    +             {topNav.tab == 'status' && projectSummary ? (
    +                 <div className=\"managed-folder-settings h100 vertical-flex\" object-metrics=\"\" project-metrics-main=\"\">
    +                     <div className=\"top-level-tabs\">
    +                         <div className=\"row-fluid object-nav\">
    +                             <div>
    +                                 <a
    +                                     className=\"{'enabled': $state.includes('projects.project.home.status.metrics')}\"
    +                                     href={$state.href('projects.project.home.status.metrics')}>
    +                                     查看
    +                                 </a>
    +                                 <a
    +                                     className=\"{'enabled': $state.includes('projects.project.home.status.checks')}\"
    +                                     href={$state.href('projects.project.home.status.checks')}>
    +                                     检查
    +                                 </a>
    +                             </div>
    +                         </div>
    +                     </div>
    + 
    +                     {metricsCallbacks ? <div className=\"dss-page\" ui-view=\"\" /> : null}
    +                 </div>
    +             ) : null}
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;