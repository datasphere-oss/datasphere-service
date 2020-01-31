import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return notebookParams ? (
    +         <div className=\"container-fluid h100 summary-tab page-top-padding fh\">
    +             <div className=\"row-fluid h100\">
    +                 <div className=\"span9 h100 oa\" style=\"padding-right: 5px\">
    +                     <div
    +                         className=\"summary\"
    +                         editable-summary=\"\"
    +                         editable=\"canWriteProject()\"
    +                         object=\"notebookParams\"
    +                         object-interest=\"objectInterest\"
    +                         object-type=\"SQL_NOTEBOOK\"
    +                         get-tags=\"getTags\"
    +                         tag-color=\"tagColor\"
    +                     />
    +                 </div>
    +                 <div className=\"span3 h100\">
    +                     <div
    +                         object-timeline-with-post=\"\"
    +                         object-type=\"'SQL_NOTEBOOK'\"
    +                         project-key=\"notebookParams.projectKey\"
    +                         object-id=\"notebookParams.id\"
    +                         initial-timeline=\"objectTimeline\"
    +                         className=\"h100\"
    +                     />
    +                 </div>
    +             </div>
    +         </div>
    +     ) : null;
    + };
    + 
    + export default TestComponent;