import * as React from 'react';
     
     export interface TestComponentProps {
         [key: string]: any;
     }
     
     const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
         return (
             <table className=\"dashboard-miniature\">
                 <tbody>
                     <tr>
                         {[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12].map((i, index: number) => {
                             return <th key={`item-${index}`} />;
                         })}
                     </tr>
                     {cells.map((row, index: number) => {
                         return (
                             <tr key={`item-${index}`}>
                                 {cell
                                     ? row.map((cell, index: number) => {
                                           return (
                                               <td
                                                   key={`item-${index}`}
                                                   colSpan={cell == 'filler' ? 1 : cell.box.width}
                                                   rowSpan={cell == 'filler' ? 1 : cell.box.height}
                                                   className={`${
                                                       cell == 'filler'
                                                           ? 'filler-cell'
                                                           : insightTypeToColor(cell.insightType)
                                                   } universe-background`}
                                                   title={cell.insightType}
                                               />
                                           );
                                       })
                                     : null}
                             </tr>
                         );
                     })}
                 </tbody>
             </table>
         );
     };
     
     export default TestComponent;