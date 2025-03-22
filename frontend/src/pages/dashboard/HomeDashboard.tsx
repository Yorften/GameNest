import React from 'react'

type Props = {}

export default function HomeDashboard({ }: Props) {
  return (
    <>
      <div className="flex justify-center bg-dashboard-primary py-10 p-14">
        <div className="container mx-auto pr-4">
          <div className="w-72 bg-dashboard-secondary max-w-xs mx-auto border border-primary rounded-sm overflow-hidden shadow-lg hover:shadow-2xl transition duration-500 transform hover:scale-100 cursor-pointer">
            <div className="h-20 bg-white/10 flex items-center justify-between">
              <p className="mr-0 text-dashboard-primabg-dashboard-secondary text-lg pl-5">BT SUBSCRIBERS</p>
            </div>
            <div className="flex justify-between px-5 pt-6 mb-2 text-sm text-gray-600">
              <p>TOTAL</p>
            </div>
            <p className="py-4 text-3xl ml-5">20,456</p>
          </div>
        </div>

        <div className="container mx-auto pr-4">
          <div className="w-72 bg-dashboard-secondary max-w-xs mx-auto border border-primary rounded-sm overflow-hidden shadow-lg hover:shadow-2xl transition duration-500 transform hover:scale-100 cursor-pointer">
            <div className="h-20 bg-white/10 flex items-center justify-between">
              <p className="mr-0 text-dashboard-primabg-dashboard-secondary text-lg pl-5">BT ACTIVE SUBSCRIBERS</p>
            </div>
            <div className="flex justify-between px-5 pt-6 mb-2 text-sm text-gray-600">
              <p>TOTAL</p>
            </div>
            <p className="py-4 text-3xl ml-5">19,694</p>
          </div>
        </div>

        <div className="container mx-auto pr-4">
          <div className="w-72 bg-dashboard-secondary max-w-xs mx-auto border border-primary rounded-sm overflow-hidden shadow-lg hover:shadow-2xl transition duration-500 transform hover:scale-100 cursor-pointer">
            <div className="h-20 bg-white/10 flex items-center justify-between">
              <p className="mr-0 text-dashboard-primabg-dashboard-secondary text-lg pl-5">BT OPT OUTS</p>
            </div>
            <div className="flex justify-between pt-6 px-5 mb-2 text-sm text-gray-600">
              <p>TOTAL</p>
            </div>
            <p className="py-4 text-3xl ml-5">711</p>
          </div>
        </div>

        <div className="container mx-auto">
          <div className="w-72 bg-dashboard-secondary max-w-xs mx-auto border border-primary rounded-sm overflow-hidden shadow-lg hover:shadow-2xl transition duration-500 transform hover:scale-100 cursor-pointer">
            <div className="h-20 bg-white/10 flex items-center justify-between">
              <p className="mr-0 text-dashboard-primabg-dashboard-secondary text-lg pl-5">BT TODAY'S SUBSCRIPTION</p>
            </div>
            <div className="flex justify-between pt-6 px-5 mb-2 text-sm text-gray-600">
              <p>TOTAL</p>
            </div>
            <p className="py-4 text-3xl ml-5">0</p>
          </div>
        </div>
      </div>
      <div className="flex justify-center bg-dashboard-primary py-10 p-5">
        <div className="container mr-5 ml-2 mx-auto bg-dashboard-secondary shadow-xl">
          <div className="w-11/12 mx-auto">
            <div className="bg-dashboard-secondary my-6">
              <table className="text-left w-full border-collapse">
                <thead>
                  <tr>
                    <th className="py-4 px-6 bg-white/5 font-bold uppercase text-sm text-white border-b border-grey-light">KEYWORDS</th>
                    <th className="py-4 px-6 text-center bg-white/5 font-bold uppercase text-sm text-white border-b border-grey-light">TOTAL ENTERIES</th>
                  </tr>
                </thead>
                <tbody>
                  <tr className="hover:bg-grey-lighter">
                    <td className="py-4 px-6 border-b border-grey-light">Bible</td>
                    <td className="py-4 px-6 text-center border-b border-grey-light">
                      11980
                    </td>
                  </tr>
                  <tr className="hover:bg-grey-lighter">
                    <td className="py-4 px-6 border-b border-grey-light">Blah</td>
                    <td className="py-4 px-6 text-center border-b border-grey-light">
                      340
                    </td>
                  </tr>
                  <tr className="hover:bg-grey-lighter">
                    <td className="py-4 px-6 border-b border-grey-light">Blah</td>
                    <td className="py-4 px-6 text-center border-b border-grey-light">
                      901
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>

        <div className="container mr-5 mx-auto bg-dashboard-secondary shadow-xl">
          <div className="w-11/12 mx-auto">
            <div className="bg-dashboard-secondary my-6">
              <table className="text-left w-full border-collapse">
                <thead>
                  <tr>
                    <th className="py-4 px-6 bg-white/5 font-bold uppercase text-sm text-white border-b border-grey-light">MSISDN</th>
                    <th className="py-4 px-6 text-center bg-white/5 font-bold uppercase text-sm text-white border-b border-grey-light">ENTRIES</th>
                  </tr>
                </thead>
                <tbody>
                  <tr className="hover:bg-grey-lighter">
                    <td className="py-4 px-6 border-b border-grey-light">26809304030</td>
                    <td className="py-4 px-6 text-center border-b border-grey-light">
                      495,455
                    </td>
                  </tr>
                  <tr className="hover:bg-grey-lighter">
                    <td className="py-4 px-6 border-b border-grey-light">26809304030</td>
                    <td className="py-4 px-6 text-center border-b border-grey-light">
                      495,455
                    </td>
                  </tr>
                  <tr className="hover:bg-grey-lighter">
                    <td className="py-4 px-6 border-b border-grey-light">26809304030</td>
                    <td className="py-4 px-6 text-center border-b border-grey-light">
                      495,455
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>

        <div className="container mx-auto bg-dashboard-secondary shadow-xl">
          <div className="w-11/12 mx-auto">
            <div className="bg-dashboard-secondary my-6">
              <table className="text-left w-full border-collapse">
                <thead>
                  <tr>
                    <th className="py-4 px-6 bg-white/5 font-bold uppercase text-sm text-white border-b border-grey-light">MSISDN</th>
                    <th className="py-4 px-6 text-center bg-white/5 font-bold uppercase text-sm text-white border-b border-grey-light">POINTS</th>
                  </tr>
                </thead>
                <tbody>
                  <tr className="hover:bg-grey-lighter">
                    <td className="py-4 px-6 border-b border-grey-light">28679609009</td>
                    <td className="py-4 text-center px-6 border-b border-grey-light">
                      11,290
                    </td>
                  </tr>
                  <tr className="hover:bg-grey-lighter">
                    <td className="py-4 px-6 border-b border-grey-light">28679609009</td>
                    <td className="py-4 text-center px-6 border-b border-grey-light">
                      9,230
                    </td>
                  </tr>
                  <tr className="hover:bg-grey-lighter">
                    <td className="py-4 px-6 border-b border-grey-light">28679609009</td>
                    <td className="py-4 px-6 text-center border-b border-grey-light">
                      234
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </>
  )
}