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
    +             shaker-facets=\"\"
    +             shaker-explore-base=\"\"
    +             analysis-script=\"\"
    +             shaker-on-dataset=\"\"
    +             shaker-with-processors=\"\"
    +             shaker-with-library=\"\"
    +             has-custom-formula-zone=\"\"
    +             show-left-pane=\"true\"
    +             fixed-panes=\"\"
    +             global-keydown=\"{'ctrl-s meta-s':'saveOnly()'}\"
    +             listen-keydown=\"{'delete': 'deleteSelectedSteps($event)' }\">
    +             <div className=\"{sampling: uiState.shakerLeftPane == 'design-sample'}\">
    +                 <div
    +                     className=\"handle\"
    +                     onClick={() => {
    +                         toggleLeftPane();
    +                     }}
    +                 />
    +                 <tabs>
    +                     <pane title=\"Script\" subtitle=\"getScriptDesc()\">
    +                         <div include-no-scope=\"/templates/shaker/shaker-script-tab.html\">
    +                             <pane title=\"Design Sample\" subtitle=\"getSampleDesc()\">
    +                                 <div include-no-scope=\"/templates/shaker/sampling-tab.html\" />
    + 
    +                                 {shakerState.activeView == 'table' ? (
    +                                     <div className=\"mainPane\">
    +                                         {/* Main pane top bar */}
    +                                         {shaker ? (
    +                                             <div className=\"table-view-header\">
    +                                                 {/* Big top bar */}
    +                                                 <div className=\"shaker-summary-wrapper\" custom-formula-zone=\"replace\">
    +                                                     {/* Step summury */}
    +                                                     <div include-no-scope=\"/templates/shaker/shaker-summary-stats.html\">
    +                                                         {/* Views Buttons */}
    +                                                         <div className=\"view-buttons-wrapper\">
    +                                                             {table.warnings.totalCount ? (
    +                                                                 <a
    +                                                                     style=\"font-size: 27px; vertical-align: middle; text-decoration: none\"
    +                                                                     onClick={() => {
    +                                                                         showWarningsDetails();
    +                                                                     }}
    +                                                                     className=\"text-warning\">
    +                                                                     <i
    +                                                                         className=\"icon-warning-sign\"
    +                                                                         toggle=\"tooltip\"
    +                                                                         title=\"Warnings occurred while previewing\"
    +                                                                     />
    +                                                                 </a>
    +                                                             ) : null}
    +                                                             <i
    +                                                                 className=\"icon-warning-sign\"
    +                                                                 toggle=\"tooltip\"
    +                                                                 title=\"Warnings occurred while previewing\">
    +                                                                 <div
    +                                                                     include-no-scope=\"/templates/shaker/display-mode-menu.html\"
    +                                                                     className=\"dib\">
    +                                                                     <div
    +                                                                         include-no-scope=\"/templates/shaker/shaker-views-buttons.html\"
    +                                                                         className=\"dib\">
    +                                                                         {appConfig.patternsLearningEnabled &&
    +                                                                         !shaker.suggestionEngine.isRecording ? (
    +                                                                             <div className=\"btn-group\">
    +                                                                                 <a
    +                                                                                     className=\"btn btn-default\"
    +                                                                                     style=\"color: #333; text-decoration: none;\"
    +                                                                                     onClick={() => {
    +                                                                                         startRecording();
    +                                                                                     }}>
    +                                                                                     <i
    +                                                                                         style=\"color:rgb(210, 0, 0)\"
    +                                                                                         className=\"icon-circle\">
    +                                                                                         &nbsp;Start pattern recording
    +                                                                                     </i>
    +                                                                                 </a>
    +                                                                                 <i
    +                                                                                     style=\"color:rgb(210, 0, 0)\"
    +                                                                                     className=\"icon-circle\"
    +                                                                                 />
    +                                                                             </div>
    +                                                                         ) : null}
    +                                                                         <i
    +                                                                             style=\"color:rgb(210, 0, 0)\"
    +                                                                             className=\"icon-circle\">
    +                                                                             {appConfig.patternsLearningEnabled &&
    +                                                                             shaker.suggestionEngine.isRecording ? (
    +                                                                                 <div className=\"btn-group\">
    +                                                                                     <a
    +                                                                                         className=\"btn btn-default\"
    +                                                                                         style=\"color: #333; text-decoration: none;\"
    +                                                                                         onClick={() => {
    +                                                                                             stopRecording();
    +                                                                                         }}>
    +                                                                                         <i
    +                                                                                             style=\"color:rgb(210, 0, 0)\"
    +                                                                                             className=\"icon-sign-blank\">
    +                                                                                             &nbsp;Stop pattern recording
    +                                                                                         </i>
    +                                                                                     </a>
    +                                                                                     <i
    +                                                                                         style=\"color:rgb(210, 0, 0)\"
    +                                                                                         className=\"icon-sign-blank\"
    +                                                                                     />
    +                                                                                 </div>
    +                                                                             ) : null}
    +                                                                             <i
    +                                                                                 style=\"color:rgb(210, 0, 0)\"
    +                                                                                 className=\"icon-sign-blank\"
    +                                                                             />
    +                                                                         </i>
    +                                                                     </div>
    +                                                                     <i
    +                                                                         style=\"color:rgb(210, 0, 0)\"
    +                                                                         className=\"icon-circle\">
    +                                                                         <i
    +                                                                             style=\"color:rgb(210, 0, 0)\"
    +                                                                             className=\"icon-sign-blank\"
    +                                                                         />
    +                                                                     </i>
    +                                                                 </div>
    +                                                                 <i style=\"color:rgb(210, 0, 0)\" className=\"icon-circle\">
    +                                                                     <i
    +                                                                         style=\"color:rgb(210, 0, 0)\"
    +                                                                         className=\"icon-sign-blank\">
    +                                                                         {/* filters bar */}
    +                                                                         <div include-no-scope=\"/templates/shaker/filters-column.html\" />
    +                                                                     </i>
    +                                                                 </i>
    +                                                             </i>
    +                                                         </div>
    +                                                         <i
    +                                                             className=\"icon-warning-sign\"
    +                                                             toggle=\"tooltip\"
    +                                                             title=\"Warnings occurred while previewing\">
    +                                                             <i style=\"color:rgb(210, 0, 0)\" className=\"icon-circle\">
    +                                                                 <i
    +                                                                     style=\"color:rgb(210, 0, 0)\"
    +                                                                     className=\"icon-sign-blank\">
    +                                                                     {/* Main pane main zone */}
    +                                                                     <div
    +                                                                         className=\"table-view\"
    +                                                                         style=\"flex: 1; display: flex; flex-direction: column;\">
    +                                                                         {fatalAPIError ? (
    +                                                                             <div className=\"fatalAPIErrorContainer\">
    +                                                                                 <BlockApiError />
    +                                                                             </div>
    +                                                                         ) : null}
    + 
    +                                                                         <div
    +                                                                             future-waiting=\"\"
    +                                                                             response=\"future\"
    +                                                                             dku-if=\"future &amp;&amp; !future.hasResult\"
    +                                                                         />
    + 
    +                                                                         {shakerState.runError ? (
    +                                                                             <div api-error-alert=\"shakerState.runError\">
    +                                                                                 {/* SHAKER TABLE */}
    +                                                                                 {!shakerState.runError ? (
    +                                                                                     <div
    +                                                                                         className={`positionTable shakerTable coloring-${
    +                                                                                             table.coloringScheme
    +                                                                                         }`}
    +                                                                                         fattable=\"\"
    +                                                                                         fattable-data=\"table\"
    +                                                                                         fat-draggable=\"\"
    +                                                                                     />
    +                                                                                 ) : null}
    + 
    +                                                                                 {pendingRequests.length &&
    +                                                                                 spinnerPosition == 'shaker' ? (
    +                                                                                     <spinner />
    +                                                                                 ) : null}
    +                                                                             </div>
    +                                                                         ) : null}
    +                                                                     </div>
    +                                                                     {shakerState.activeView == 'columns' ? (
    +                                                                         <div
    +                                                                             className=\"mainPane h100\"
    +                                                                             analysis-columns=\"\">
    +                                                                             <div
    +                                                                                 className=\"h100\"
    +                                                                                 include-no-scope=\"/templates/shaker/columns-view.html\"
    +                                                                             />
    +                                                                             <div
    +                                                                                 quick-columns-view=\"\"
    +                                                                                 className=\"rightPane\"
    +                                                                             />
    +                                                                             <div include-no-scope=\"/templates/shaker/processors-library.html\" />
    +                                                                             <div
    +                                                                                 coachmark-serie-caller=\"\"
    +                                                                                 serie-id=\"'shaker-hello'\"
    +                                                                                 extra-serie-ids=\"extraCoachmarksSerieIds\"
    +                                                                             />
    + 
    +                                                                             <div include-no-scope=\"/templates/shaker/script-modals.html\" />
    +                                                                         </div>
    +                                                                     ) : null}
    +                                                                 </i>
    +                                                             </i>
    +                                                         </i>
    +                                                     </div>
    +                                                 </div>
    +                                             </div>
    +                                         ) : null}
    +                                     </div>
    +                                 ) : null}
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