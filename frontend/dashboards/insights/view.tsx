import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return insight ? (
    +         <div ng-switch=\"insight.type\" className=\"{'full-screen': uiState.fullScreen}\">
    +             <div ng-switch-when=\"dataset_table\" dataset-table-insight-view=\"\" insight=\"insight\">
    +                 <div ng-switch-when=\"chart\" chart-insight-view=\"\" insight=\"insight\" className=\"h100\">
    +                     <div ng-switch-when=\"jupyter\" jupyter-insight-view=\"\" insight=\"insight\" className=\"h100\">
    +                         <div ng-switch-when=\"metrics\" metrics-insight-view=\"\" insight=\"insight\" className=\"h100\">
    +                             <div
    +                                 ng-switch-when=\"dataset_table\"
    +                                 dataset-table-insight-view=\"\"
    +                                 insight=\"insight\"
    +                                 className=\"h100\">
    +                                 <div
    +                                     ng-switch-when=\"managed-folder_content\"
    +                                     managed-folder-content-insight-view=\"\"
    +                                     insight=\"insight\"
    +                                     className=\"h100\">
    +                                     <div
    +                                         ng-switch-when=\"web_app\"
    +                                         web-app-insight-view=\"\"
    +                                         insight=\"insight\"
    +                                         className=\"h100\">
    +                                         <div
    +                                             ng-switch-when=\"report\"
    +                                             report-insight-view=\"\"
    +                                             insight=\"insight\"
    +                                             className=\"h100\">
    +                                             <div
    +                                                 ng-switch-when=\"scenario_last_runs\"
    +                                                 scenario-last-runs-insight-view=\"\"
    +                                                 insight=\"insight\"
    +                                                 className=\"h100\">
    +                                                 <div
    +                                                     ng-switch-when=\"saved-model_report\"
    +                                                     saved-model-report-insight-view=\"\">
    +                                                     <div
    +                                                         ng-switch-when=\"discussions\"
    +                                                         discussions-insight-view=\"\"
    +                                                         className=\"h100\">
    +                                                         <div ng-switch-when=\"static_file\" static-file-insight-view=\"\">
    +                                                             <div
    +                                                                 ng-switch-when=\"project_activity\"
    +                                                                 project-activity-insight-view=\"\">
    +                                                                 <div
    +                                                                     ng-switch-when=\"runnable-button\"
    +                                                                     runnable-button-insight-view=\"\"
    +                                                                     insight=\"insight\"
    +                                                                     className=\"h100\">
    +                                                                     <div
    +                                                                         ng-switch-when=\"article\"
    +                                                                         article-insight-view=\"\"
    +                                                                         insight=\"insight\"
    +                                                                         className=\"h100\"
    +                                                                     />
    +                                                                 </div>
    +                                                             </div>
    +                                                         </div>
    +                                                     </div>
    +                                                 </div>
    +                                             </div>
    +                                         </div>
    +                                     </div>
    +                                 </div>
    +                             </div>
    +                         </div>
    +                     </div>
    +                 </div>
    +             </div>
    +         </div>
    +     ) : null;
    + };
    + 
    + export default TestComponent;