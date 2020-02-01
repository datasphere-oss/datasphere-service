import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return selection.filteredObjects.length > 0 && mlTaskDesign ? (
    +         <div className=\"fh models-list-view\">
    +             <div className=\"row-fluid fh\">
    +                 {/* Session selector */}
    +                 {!sessionTask.tensorboardStatus.fullScreen ? (
    +                     <div
    +                         className=\"span3 offset0 h100 oa session-selector\"
    +                         tabIndex={0}
    +                         style=\"outline:none\"
    +                         onKeyDown={(event: React.SyntheticEvent<HTMLElement>) => {
    +                             multiSelectKeydown(event, true);
    +                         }}>
    +                         <div include-no-scope=\"/templates/analysis/models-list-view-master.html\" />
    + 
    +                         {/* Models list (Template different for KERAS backend) */}
    +                         {!isMLBackendType('KERAS') ? (
    +                             <div
    +                                 className=\"h100\"
    +                                 include-no-scope=\"/templates/analysis/models-list-view-regular.html\"
    +                             />
    +                         ) : null}
    +                         {isMLBackendType('KERAS') ? (
    +                             <div className=\"h100\" include-no-scope=\"/templates/analysis/models-list-view-keras.html\" />
    +                         ) : null}
    +                     </div>
    +                 ) : null}
    +             </div>
    +             {selection.filteredObjects.length === 0 ? (
    +                 <div className=\"empty\">
    +                     <div>无可用模型</div>
    +                     {mlTaskStatus.fullModelIds.length > 0 ? (
    +                         <span>
    +                             <a
    +                                 onClick={() => {
    +                                     clearFilters();
    +                                 }}>
    +                                 Clear filters
    +                             </a>{' '}
    +                             to see more
    +                         </span>
    +                     ) : null}
    +                 </div>
    +             ) : null}
    +         </div>
    +     ) : null;
    + };
    + 
    + export default TestComponent;