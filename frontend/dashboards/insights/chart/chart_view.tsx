import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"charts-container shaker-visualize big-chart h100\">
    +             <div chart-configuration=\"\" className=\"h100 chart-configuration-wrapper\" />
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;