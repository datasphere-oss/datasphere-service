import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"dku-carousel\">
    +             <a
    +                 className=\"controller controller-left\"
    +                 onClick={() => {
    +                     slideLeft();
    +                 }}>
    +                 <i className=\"icon icon-chevron-left\" />
    +             </a>
    +             <div className=\"slides-frame\">
    +                 <div className=\"slider\">
    +                     {entries.map((entry, index: number) => {
    +                         return <div key={`item-${index}`} className=\"{'current' : $index == index}\" dku-inject=\"\" />;
    +                     })}
    +                 </div>
    +             </div>
    +             <a
    +                 className=\"controller controller-right\"
    +                 onClick={() => {
    +                     slideRight();
    +                 }}>
    +                 <i className=\"icon icon-chevron-right\" />
    +             </a>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;