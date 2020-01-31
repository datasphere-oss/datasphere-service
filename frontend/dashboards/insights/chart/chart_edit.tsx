import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div
    +             className=\"{showLeftPane: showLeftPane, showRightPane: showRightPane}\"
    +             fixed-panes=\"\"
    +             show-left-pane=\"true\"
    +             chart-multi-drag-drop-zones=\"\">
    +             <div className=\"leftPane\">
    +                 <tabs>
    +                     <pane title=\"Columns\" no-resize-hack=\"no\">
    +                         <div include-no-scope=\"/templates/shaker/charts-available-measures.html\">
    +                             <pane title=\"Sampling &amp; Engine\" no-resize-hack=\"no\">
    +                                 {dataset && chart ? (
    +                                     <div>
    +                                         <div dataset-chart-sampling-editor=\"\" chart=\"chart\" dataset=\"dataset\" />
    +                                     </div>
    +                                 ) : null}
    +                                 <div className=\"mainPane\">
    +                                     <div className=\"h100\">
    +                                         <div className=\"vertical-flex h100\">
    +                                             <div className=\"flex\" ng-switch=\"\" on=\"currentInsight\">
    +                                                 <div className=\"fh\" ng-switch-default=\"\">
    +                                                     <div
    +                                                         className=\"h100 chart-configuration-wrapper\"
    +                                                         chart-configuration=\"\"
    +                                                     />
    +                                                 </div>
    +                                             </div>
    +                                         </div>
    +                                     </div>
    +                                 </div>
    +                             </pane>
    +                         </div>
    +                     </pane>
    +                 </tabs>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;