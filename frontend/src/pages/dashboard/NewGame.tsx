import { ChangeEvent, useEffect, useState } from 'react';
import { useAppDispatch, useAppSelector } from '../../app/hooks';
import Button from '../../components/miscs/Button';
import { selectCurrentUser, updateUserInstallationId } from '../../features/auth/authSlice';
import GithubButton from '../../components/miscs/GithubButton';

type Props = {}

const versionRegex = /^\d+\.\d+\.\d+$/;

export default function NewGame({ }: Props) {
  const dispatch = useAppDispatch();
  const user = useAppSelector(selectCurrentUser);

  // Form fields
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [version, setVersion] = useState('');

  // Field-level errors
  const [titleError, setTitleError] = useState('');
  const [descriptionError, setDescriptionError] = useState('');
  const [versionError, setVersionError] = useState('');

  const handleTitleChange = (e: ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setTitle(value);
    if (!value.trim()) {
      setTitleError("Title is required.");
    } else {
      setTitleError("");
    }
  };
  const handleDescriptionChange = (e: ChangeEvent<HTMLTextAreaElement>) => {
    const value = e.target.value;
    setDescription(value);
    if (!value.trim()) {
      setDescriptionError("Description is required.");
    } else {
      setDescriptionError("");
    }
  };
  const handleVersionChange = (e: ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setVersion(value);
    if (!value.trim()) {
      setVersionError("Version is required.");
    } else if (!versionRegex.test(value)) {
      setVersionError("Please enter a valid version format (x.x.x)).");
    } else {
      setVersionError("");
    }
  };

  // Server/global error
  const [serverError, setServerError] = useState('');

  function handleSubmit(e: React.MouseEvent): void {
    e.preventDefault();

    let isValid = true;
    if (!title.trim()) {
      setTitleError("Username is required.");
      isValid = false;
    }
    if (!description.trim()) {
      setDescriptionError("Username is required.");
      isValid = false;
    }
    if (!version.trim()) {
      setVersionError("Username is required.");
      isValid = false;
    } else if (!versionRegex.test(version)) {
      setVersionError("Please enter a valid version format (x.x.x)).");
    }

    if (!isValid) return;
  }

  return (
    <>
      <div className="flex items-center justify-between mb-4 px-4 pt-4">
        <h2 className="text-3xl font-semibold">Build a new game</h2>
        <Button
          onClick={handleSubmit}
          className="!py-1 !px-10 hover:!brightness-200 !bg-gray-50/10 border border-primary"
          title="Build"
          type="submit"
          form="gameForm"
        />
      </div>
      <div className='px-4'>
        <form id="gameForm" className='flex flex-col md:flex-row gap-10'>
          {serverError && (
            <p className="text-red-500 text-sm mb-2">
              {serverError}
            </p>
          )}
          <div className='flex flex-col gap-4 w-full md:w-3/5'>
            <div>
              <label htmlFor="title" className="block mb-1 font-semibold">
                Title:
              </label>
              <input
                id="title"
                className="w-full rounded border border-primary bg-dashboard-secondary placeholder:text-white/50 px-3 py-2 text-white/90"
                placeholder="Enter a title for your game..."
                value={title}
                onChange={handleTitleChange}
              />
              {titleError && <p className="text-red-500 text-sm">{titleError}</p>}
            </div>

            <div>
              <label htmlFor="description" className="block mb-1 font-semibold">
                Description:
              </label>
              <textarea
                id="description"
                className="w-full rounded border border-primary bg-dashboard-secondary placeholder:text-white/50 px-3 py-2 text-white/90"
                placeholder="A short description of your game..."
                rows={3}
                value={description}
                onChange={handleDescriptionChange}
              />
              {descriptionError && (
                <p className="text-red-500 text-sm">{descriptionError}</p>
              )}
            </div>

            <div>
              <label htmlFor="version" className="block mb-1 font-semibold">
                Version:
              </label>
              <input
                id="version"
                className="w-full rounded border border-primary bg-dashboard-secondary placeholder:text-white/50 px-3 py-2 text-white/90"
                placeholder="e.g. 1.0.0"
                value={version}
                onChange={handleVersionChange}
              />
              {versionError && <p className="text-red-500 text-sm">{versionError}</p>}
            </div>
          </div>
          <div className='w-full flex justify-center md:w-2/5 p-4'>
            {user?.installationId ? (
              <p>Select input to pick repository</p>
            ) : (
              <div className='pt-2'>  
                <GithubButton />
              </div>
            )}
          </div>

        </form>
      </div>
    </>
  )
}