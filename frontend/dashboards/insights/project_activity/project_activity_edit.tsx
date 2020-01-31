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
    +                     <pane title=\"Columns\" icon=\"reorder icon-rotate-90\" no-resize-hack=\"no\">
    +                         <div include-no-scope=\"/templates/shaker/charts-available-measures.html\">
    +                             <pane title=\"Sampling\" icon=\"code-fork\" no-resize-hack=\"no\">
    +                                 <div
    +                                     chart-sampling-editor=\"\"
    +                                     sampling=\"insight.params.dataView.sampling\"
    +                                     dataset=\"dataset\"
    +                                     is-dangerous=\"isDangerous\"
    +                                     can-use-sql=\"canUseSQL\"
    +                                     on-change=\"samplingChangedHandler\"
    +                                 />
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