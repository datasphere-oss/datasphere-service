import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"column-header\">
    +             <div className=\"name\">{cell.name}</div>
    +             <span className=\"type\">{cell.type}</span>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;