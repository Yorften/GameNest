import { useSearchParams } from "react-router";
import { useAppDispatch } from "../app/hooks";
import { useEffect } from "react";

type Props = {}

export default function CallBackPage({ }: Props) {
  const [searchParams] = useSearchParams();

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