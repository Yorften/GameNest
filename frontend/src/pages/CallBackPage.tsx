import { useSearchParams } from "react-router";
import { useAppDispatch } from "../app/hooks";
import { useEffect } from "react";

type Props = {}

export default function CallBackPage({ }: Props) {
  const dispatch = useAppDispatch();
  const [searchParams] = useSearchParams();

  const installationId = searchParams.get('installation_id');

  // useEffect(() => {
  //   if (searchParams.get("setup_action") === "install") {
  //     if (installationId) {
  //       dispatch(updateUserInstallationId(installationId as unknown as number))
  //         .unwrap()
  //         .then(() => {
  //           window.close();
  //         })
  //         .catch((err) => {
  //           console.error('Failed to update installation ID:', err);
  //         });
  //     }
  //   }
  // }, [searchParams, dispatch]);

  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const installationId = urlParams.get('installation_id');

    if (installationId && window.opener) {
      window.opener.postMessage(
        { type: 'github-installation', installationId },
        window.location.origin
      );
    }
    window.close();
  }, []);


  return <></>;
}